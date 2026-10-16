package com.example.data.model

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val educationBoard: String = "GSEB",
    val classLevel: String = "Class 10",
    val targetExamYear: Int = 2026,
    val preferredLanguage: String = "English",
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

data class SavedBookmark(
    val id: String = "",
    val userId: String = "",
    val resourceId: String = "",
    val title: String = "",
    val board: String = "",
    val classLevel: String = "",
    val subject: String = "",
    val resourceType: String = "",
    val savedAt: Long = System.currentTimeMillis()
)

data class QuizResultDoc(
    val id: String = "",
    val userId: String = "",
    val quizTitle: String = "",
    val subject: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val percentage: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)

data class LearningProgressDoc(
    val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val lessonId: String = "",
    val isCompleted: Boolean = true,
    val score: Int = 100,
    val completedAt: Long = System.currentTimeMillis()
)

data class SavedMockPaperDoc(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val board: String = "",
    val classLevel: String = "",
    val subject: String = "",
    val totalMarks: Int = 0,
    val durationMinutes: Int = 0,
    val generatedAt: Long = System.currentTimeMillis(),
    val jsonContent: String = ""
)

data class SavedReportDoc(
    val id: String = "",
    val userId: String = "",
    val generatedAt: Long = System.currentTimeMillis(),
    val totalCompletedLessons: Int = 0,
    val totalQuizzesTaken: Int = 0,
    val averageScorePercentage: Float = 0f,
    val summaryJson: String = ""
)
