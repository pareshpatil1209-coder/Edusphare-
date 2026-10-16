package com.example.data.model

data class EducationalResource(
    val id: String,
    val title: String,
    val board: String, // "GSEB", "CBSE", "State Board"
    val classLevel: String, // "Class 9", "Class 10", "Class 11 Science", "Class 12 Science"
    val subject: String,
    val chapter: String,
    val resourceType: ResourceType,
    val language: String, // "English", "Gujarati", "Hindi"
    val publisher: String, // "Gujarat State Board of School Textbooks (GSSTB)", "NCERT", "DIKSHA"
    val officialSourceUrl: String,
    val licenseNotice: String,
    val description: String,
    val fullTextContent: String,
    val estimatedReadMinutes: Int = 12,
    val isVerifiedSource: Boolean = true
)

enum class ResourceType(val displayName: String) {
    E_TEXTBOOK("E-Textbook"),
    WORKBOOK("Workbook"),
    STUDY_NOTE("Study Note"),
    PREVIOUS_YEAR_PAPER("Previous Year Paper"),
    BLUEPRINT("Exam Blueprint")
}

data class MockPaperConfig(
    val board: String = "GSEB",
    val classLevel: String = "Class 10",
    val subject: String = "Mathematics",
    val chapters: List<String> = listOf("All Chapters"),
    val language: String = "English",
    val totalMarks: Int = 50,
    val durationMinutes: Int = 90,
    val difficulty: String = "Standard",
    val includeMCQ: Boolean = true,
    val includeShortAnswer: Boolean = true,
    val includeLongAnswer: Boolean = true
)

data class GeneratedMockPaper(
    val id: String,
    val title: String,
    val board: String,
    val classLevel: String,
    val subject: String,
    val language: String,
    val totalMarks: Int,
    val durationMinutes: Int,
    val difficulty: String,
    val generalInstructions: List<String>,
    val sections: List<ExamSection>,
    val markingScheme: List<AnswerKeyItem>,
    val generatedAtMillis: Long = System.currentTimeMillis(),
    val isAiGenerated: Boolean = true,
    val disclaimer: String = "This is an AI-generated practice mock paper aligned with the official board curriculum blueprint. It is not an official leaked examination question paper."
)

data class ExamSection(
    val sectionName: String, // "Section A (Objective)", "Section B (Short Answer)"
    val instructions: String,
    val totalMarks: Int,
    val questions: List<ExamQuestion>
)

data class ExamQuestion(
    val questionNumber: Int,
    val prompt: String,
    val questionType: String, // "MCQ", "VSA", "SA", "LA"
    val marks: Int,
    val options: List<String>? = null,
    val chapterHint: String? = null
)

data class AnswerKeyItem(
    val questionNumber: Int,
    val expectedAnswer: String,
    val stepByStepMarking: String,
    val keyFormulas: List<String> = emptyList()
)

data class FlashcardDeck(
    val id: String,
    val title: String,
    val subject: String,
    val board: String,
    val classLevel: String,
    val cards: List<FlashcardItem>
)

data class FlashcardItem(
    val id: String,
    val front: String,
    val back: String,
    val hint: String? = null,
    val keyConcept: String? = null,
    var isMarkedForRevision: Boolean = false,
    var isMastered: Boolean = false
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val chapter: String,
    val marks: Int = 1
)

data class QuizAttempt(
    val id: String,
    val quizTitle: String,
    val subject: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val weakTopics: List<String> = emptyList()
)

data class StudyReport(
    val id: String,
    val studentName: String,
    val generatedAt: Long = System.currentTimeMillis(),
    val board: String,
    val classLevel: String,
    val totalCompletedLessons: Int,
    val totalQuizzesTaken: Int,
    val averageScorePercentage: Float,
    val subjectStats: List<SubjectProgressStat>,
    val recentActivities: List<ActivityLogItem>,
    val topicsRequiringRevision: List<String>,
    val aiStudyRecommendations: List<String>,
    val isExportableToPdf: Boolean = true
)

data class SubjectProgressStat(
    val subjectName: String,
    val completedTopics: Int,
    val totalTopics: Int,
    val averageScore: Float,
    val progressFraction: Float
)

data class ActivityLogItem(
    val activityType: String,
    val title: String,
    val details: String,
    val timestamp: Long
)
