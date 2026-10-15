package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val preferredLanguage: String? = "English",
    val educationBoard: String? = "CBSE",
    val classLevel: String? = "Class 10",
    val stream: String? = "General",
    val onboardingCompleted: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class BookmarkItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val type: String = "note", // note, question, chapter, code
    val subject: String? = null,
    val referenceId: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class LearningProgressItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val courseId: String = "",
    val courseType: String = "academic", // academic, language, coding
    val completedLessons: List<String> = emptyList(),
    val progressPercent: Int = 0,
    @ServerTimestamp val lastAccessedAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class TestResultItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val testTitle: String = "",
    val subject: String = "",
    val score: Int = 0,
    val totalMarks: Int = 0,
    val accuracy: Int = 0,
    val weakTopics: List<String> = emptyList(),
    @ServerTimestamp val completedAt: Timestamp? = null
)
