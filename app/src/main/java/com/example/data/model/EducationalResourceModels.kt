package com.example.data.model

data class EducationalResource(
    val id: String,
    val title: String,
    val board: String, // "GSEB", "CBSE", "ICSE", "State Board"
    val classLevel: String, // "Class 9", "Class 10", "Class 11 Science", "Class 11 Commerce", "Class 12 Science", "Class 12 Commerce"
    val subject: String, // "Mathematics", "Science", "Physics", "Chemistry", "Biology", "Social Science", "Gujarati", "English", "Hindi", "Sanskrit", "Computer Studies"
    val chapter: String,
    val language: String, // "English", "Gujarati", "Hindi"
    val resourceType: String, // "E-Textbook", "Study Notes", "Workbook", "Previous Year Question Paper", "Sample Paper"
    val source: String, // e.g. "GSEB - Gujarat State Board of School Textbooks (GSSTB)", "NCERT Official Portal", "DIKSHA National Portal"
    val officialUrl: String, // Genuine official portal URL
    val licensing: String, // Usage/Licensing info
    val editionYear: String = "2024-2025",
    val summary: String,
    val fullContent: String
)

data class FlashcardItem(
    val id: String,
    val frontQuestion: String,
    val backAnswer: String,
    val category: String = "Core Concept",
    val hint: String? = null,
    val formulaOrRule: String? = null
)

data class FlashcardDeck(
    val id: String,
    val title: String,
    val board: String,
    val classLevel: String,
    val subject: String,
    val description: String,
    val cards: List<FlashcardItem>
)

data class MockPaperConfig(
    val board: String = "GSEB",
    val classLevel: String = "Class 10",
    val subject: String = "Mathematics",
    val chapter: String = "Full Syllabus",
    val language: String = "English",
    val totalMarks: Int = 80,
    val durationMinutes: Int = 180,
    val difficulty: String = "Standard Board", // "Standard Board", "Moderate", "Challenging"
    val questionTypes: List<String> = listOf("MCQs (1 Mark)", "Short Answer (2 Marks)", "Long Answer (3 Marks)", "Application & Numerical (4-5 Marks)")
)

data class MockPaperQuestion(
    val number: Int,
    val section: String, // "Section A", "Section B", etc.
    val question: String,
    val options: List<String>? = null,
    val marks: Int,
    val answerKeyOrSolution: String,
    val markingSchemeStep: String
)

data class MockPaperSection(
    val name: String,
    val title: String,
    val marksPerQuestion: Int,
    val totalSectionMarks: Int,
    val instructions: String,
    val questions: List<MockPaperQuestion>
)

data class GeneratedMockPaper(
    val id: String,
    val title: String,
    val board: String,
    val classLevel: String,
    val subject: String,
    val totalMarks: Int,
    val durationMinutes: Int,
    val generalInstructions: List<String>,
    val sections: List<MockPaperSection>,
    val disclaimer: String = "AI-Generated Practice Material — Aligned with official curriculum blueprint for self-evaluation. Not an official board paper.",
    val isAiGenerated: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis()
)
