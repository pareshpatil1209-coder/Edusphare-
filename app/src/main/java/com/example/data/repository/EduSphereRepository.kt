package com.example.data.repository

import android.content.Context
import com.example.data.local.EduSphereDatabase
import com.example.data.local.OfflineMaterialEntity
import com.example.data.model.ActivityLogItem
import com.example.data.model.EducationalResource
import com.example.data.model.GeneratedMockPaper
import com.example.data.model.LearningProgressDoc
import com.example.data.model.QuizAttempt
import com.example.data.model.QuizResultDoc
import com.example.data.model.SavedBookmark
import com.example.data.model.SavedMockPaperDoc
import com.example.data.model.SavedReportDoc
import com.example.data.model.StudyReport
import com.example.data.model.SubjectProgressStat
import com.example.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EduSphereRepository(context: Context) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val offlineDao = EduSphereDatabase.getDatabase(context).offlineMaterialDao()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

    private val _quizHistory = MutableStateFlow<List<QuizAttempt>>(emptyList())
    val quizHistory: StateFlow<List<QuizAttempt>> = _quizHistory.asStateFlow()

    private val _completedLessons = MutableStateFlow<Set<String>>(emptySet())
    val completedLessons: StateFlow<Set<String>> = _completedLessons.asStateFlow()

    private val _savedMockPapers = MutableStateFlow<List<GeneratedMockPaper>>(emptyList())
    val savedMockPapers: StateFlow<List<GeneratedMockPaper>> = _savedMockPapers.asStateFlow()

    private val _savedReports = MutableStateFlow<List<StudyReport>>(emptyList())
    val savedReports: StateFlow<List<StudyReport>> = _savedReports.asStateFlow()

    init {
        // Observe auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                attachUserListeners(user.uid)
            } else {
                // Initialize default student guest profile
                _currentUserProfile.value = UserProfile(
                    uid = "guest_student",
                    displayName = "Student",
                    educationBoard = "GSEB",
                    classLevel = "Class 10"
                )
            }
        }
    }

    fun getCurrentFirebaseUser(): FirebaseUser? = auth.currentUser

    private fun attachUserListeners(uid: String) {
        // Fetch/Listen to UserProfile
        firestore.collection("users").document(uid).addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                _currentUserProfile.value = snapshot.toObject(UserProfile::class.java)
            } else {
                val newProfile = UserProfile(
                    uid = uid,
                    email = auth.currentUser?.email ?: "",
                    displayName = auth.currentUser?.displayName ?: "Student",
                    photoUrl = auth.currentUser?.photoUrl?.toString() ?: "",
                    educationBoard = "GSEB",
                    classLevel = "Class 10"
                )
                _currentUserProfile.value = newProfile
                firestore.collection("users").document(uid).set(newProfile)
            }
        }

        // Listen to Quiz Results
        firestore.collection("users").document(uid).collection("quiz_results")
            .orderBy("timestamp")
            .addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    val attempts = snapshots.documents.mapNotNull { doc ->
                        val result = doc.toObject(QuizResultDoc::class.java) ?: return@mapNotNull null
                        QuizAttempt(
                            id = result.id.ifEmpty { doc.id },
                            quizTitle = result.quizTitle,
                            subject = result.subject,
                            score = result.score,
                            totalQuestions = result.totalQuestions,
                            percentage = result.percentage,
                            timestamp = result.timestamp
                        )
                    }
                    _quizHistory.value = attempts
                }
            }

        // Listen to Learning Progress
        firestore.collection("users").document(uid).collection("learning_progress")
            .addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    val lessonIds = snapshots.documents.mapNotNull { it.getString("lessonId") }.toSet()
                    _completedLessons.value = lessonIds
                }
            }
    }

    suspend fun updateUserProfile(board: String, classLevel: String, targetExamYear: Int) {
        val uid = auth.currentUser?.uid ?: "guest_student"
        val current = _currentUserProfile.value ?: UserProfile(uid = uid)
        val updated = current.copy(
            educationBoard = board,
            classLevel = classLevel,
            targetExamYear = targetExamYear,
            lastLoginAt = System.currentTimeMillis()
        )
        _currentUserProfile.value = updated

        if (auth.currentUser != null) {
            try {
                firestore.collection("users").document(uid).set(updated).await()
            } catch (e: Exception) {
                // Offline fallback handled by Firestore local cache
            }
        }
    }

    suspend fun saveQuizResult(attempt: QuizAttempt) {
        val uid = auth.currentUser?.uid
        val updatedList = _quizHistory.value.toMutableList().apply { add(attempt) }
        _quizHistory.value = updatedList

        if (uid != null) {
            try {
                val doc = QuizResultDoc(
                    id = attempt.id,
                    userId = uid,
                    quizTitle = attempt.quizTitle,
                    subject = attempt.subject,
                    score = attempt.score,
                    totalQuestions = attempt.totalQuestions,
                    percentage = attempt.percentage,
                    timestamp = attempt.timestamp
                )
                firestore.collection("users").document(uid).collection("quiz_results").document(attempt.id).set(doc).await()
            } catch (e: Exception) {
                // Ignore error if offline
            }
        }
    }

    suspend fun markLessonCompleted(courseId: String, lessonId: String, score: Int = 100) {
        val uid = auth.currentUser?.uid
        val currentSet = _completedLessons.value.toMutableSet().apply { add(lessonId) }
        _completedLessons.value = currentSet

        if (uid != null) {
            try {
                val doc = LearningProgressDoc(
                    id = "${courseId}_${lessonId}",
                    userId = uid,
                    courseId = courseId,
                    lessonId = lessonId,
                    isCompleted = true,
                    score = score,
                    completedAt = System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).collection("learning_progress").document(doc.id).set(doc).await()
            } catch (e: Exception) {
                // Offline cache
            }
        }
    }

    suspend fun saveMockPaper(paper: GeneratedMockPaper) {
        val uid = auth.currentUser?.uid
        _savedMockPapers.value = listOf(paper) + _savedMockPapers.value.filter { it.id != paper.id }

        if (uid != null) {
            try {
                val doc = SavedMockPaperDoc(
                    id = paper.id,
                    userId = uid,
                    title = paper.title,
                    board = paper.board,
                    classLevel = paper.classLevel,
                    subject = paper.subject,
                    totalMarks = paper.totalMarks,
                    durationMinutes = paper.durationMinutes,
                    generatedAt = paper.generatedAtMillis,
                    jsonContent = "${paper.title}|${paper.totalMarks}"
                )
                firestore.collection("users").document(uid).collection("saved_mock_papers").document(paper.id).set(doc).await()
            } catch (e: Exception) {
                // Offline fallback
            }
        }
    }

    suspend fun saveStudyReport(report: StudyReport) {
        val uid = auth.currentUser?.uid
        _savedReports.value = listOf(report) + _savedReports.value.filter { it.id != report.id }

        if (uid != null) {
            try {
                val doc = SavedReportDoc(
                    id = report.id,
                    userId = uid,
                    generatedAt = report.generatedAt,
                    totalCompletedLessons = report.totalCompletedLessons,
                    totalQuizzesTaken = report.totalQuizzesTaken,
                    averageScorePercentage = report.averageScorePercentage,
                    summaryJson = report.studentName
                )
                firestore.collection("users").document(uid).collection("study_reports").document(report.id).set(doc).await()
            } catch (e: Exception) {
                // Offline cache
            }
        }
    }

    // Room Database Offline Storage
    fun getAllOfflineMaterials(): Flow<List<OfflineMaterialEntity>> = offlineDao.getAllOfflineMaterials()

    fun isMaterialSavedOffline(id: String): Flow<Boolean> = offlineDao.isMaterialSaved(id)

    suspend fun saveMaterialOffline(resource: EducationalResource) {
        val entity = OfflineMaterialEntity(
            id = resource.id,
            title = resource.title,
            board = resource.board,
            classLevel = resource.classLevel,
            subject = resource.subject,
            chapter = resource.chapter,
            resourceType = resource.resourceType.name,
            language = resource.language,
            publisher = resource.publisher,
            fullTextContent = resource.fullTextContent
        )
        offlineDao.insertMaterial(entity)
    }

    suspend fun removeMaterialOffline(id: String) {
        offlineDao.deleteMaterialById(id)
    }

    fun signOut() {
        auth.signOut()
        _currentUserProfile.value = UserProfile(
            uid = "guest_student",
            displayName = "Student",
            educationBoard = "GSEB",
            classLevel = "Class 10"
        )
        _quizHistory.value = emptyList()
        _completedLessons.value = emptySet()
    }
}
