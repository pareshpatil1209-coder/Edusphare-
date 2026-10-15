package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.BookmarkItem
import com.example.data.model.LearningProgressItem
import com.example.data.model.TestResultItem
import com.example.data.model.UserProfile
import com.example.data.util.OperationType
import com.example.data.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EduSphereRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be authenticated before performing Firestore operations.")
    }

    suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        val path = "users/$uid"
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            Result.success(snapshot.toObject(UserProfile::class.java))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, path)
            Result.failure(e)
        }
    }

    fun observeUserProfile(uid: String): Flow<UserProfile?> = flow {
        val path = "users/$uid"
        emitAll(
            db.collection("users").document(uid)
                .snapshots()
                .map { it.toObject(UserProfile::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
                    throw error
                }
        )
    }

    suspend fun createOrUpdateUserProfile(profile: UserProfile): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        return try {
            val existing = docRef.get().await()
            if (existing.exists()) {
                val updates = mutableMapOf<String, Any?>(
                    "displayName" to profile.displayName,
                    "photoUrl" to profile.photoUrl,
                    "preferredLanguage" to profile.preferredLanguage,
                    "educationBoard" to profile.educationBoard,
                    "classLevel" to profile.classLevel,
                    "stream" to profile.stream,
                    "onboardingCompleted" to profile.onboardingCompleted,
                    "updatedAt" to FieldValue.serverTimestamp()
                ).filterValues { it != null }

                docRef.update(updates).await()
            } else {
                val newProfile = mutableMapOf<String, Any?>(
                    "uid" to uid,
                    "displayName" to profile.displayName,
                    "email" to profile.email,
                    "photoUrl" to profile.photoUrl,
                    "preferredLanguage" to (profile.preferredLanguage ?: "English"),
                    "educationBoard" to (profile.educationBoard ?: "CBSE"),
                    "classLevel" to (profile.classLevel ?: "Class 10"),
                    "stream" to (profile.stream ?: "General"),
                    "onboardingCompleted" to profile.onboardingCompleted,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                ).filterValues { it != null }

                docRef.set(newProfile).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    fun observeBookmarks(): Flow<List<BookmarkItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/bookmarks"
        emitAll(
            db.collection("users").document(uid).collection("bookmarks")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(BookmarkItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addBookmark(bookmark: BookmarkItem): Result<String> {
        val uid = requireUserId()
        val id = if (bookmark.id.isNotBlank()) bookmark.id else UUID.randomUUID().toString()
        val docRef = db.collection("users").document(uid).collection("bookmarks").document(id)
        return try {
            val payload = mutableMapOf<String, Any?>(
                "userId" to uid,
                "title" to bookmark.title,
                "type" to bookmark.type,
                "subject" to bookmark.subject,
                "referenceId" to bookmark.referenceId,
                "createdAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }

            docRef.set(payload).await()
            Result.success(id)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun removeBookmark(bookmarkId: String): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("bookmarks").document(bookmarkId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }

    fun observeLearningProgress(): Flow<List<LearningProgressItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/learningProgress"
        emitAll(
            db.collection("users").document(uid).collection("learningProgress")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(LearningProgressItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun saveLearningProgress(progress: LearningProgressItem): Result<Unit> {
        val uid = requireUserId()
        val id = if (progress.id.isNotBlank()) progress.id else progress.courseId
        val docRef = db.collection("users").document(uid).collection("learningProgress").document(id)
        return try {
            val payload = mutableMapOf<String, Any?>(
                "userId" to uid,
                "courseId" to progress.courseId,
                "courseType" to progress.courseType,
                "completedLessons" to progress.completedLessons,
                "progressPercent" to progress.progressPercent,
                "lastAccessedAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }

            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    fun observeTestResults(): Flow<List<TestResultItem>> = flow {
        val uid = requireUserId()
        val path = "users/$uid/testResults"
        emitAll(
            db.collection("users").document(uid).collection("testResults")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(TestResultItem::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun recordTestResult(testResult: TestResultItem): Result<String> {
        val uid = requireUserId()
        val id = if (testResult.id.isNotBlank()) testResult.id else UUID.randomUUID().toString()
        val docRef = db.collection("users").document(uid).collection("testResults").document(id)
        return try {
            val payload = mutableMapOf<String, Any?>(
                "userId" to uid,
                "testTitle" to testResult.testTitle,
                "subject" to testResult.subject,
                "score" to testResult.score,
                "totalMarks" to testResult.totalMarks,
                "accuracy" to testResult.accuracy,
                "weakTopics" to testResult.weakTopics,
                "completedAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }

            docRef.set(payload).await()
            Result.success(id)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }
}
