package com.example.feature.ai

import com.example.data.model.AnswerKeyItem
import com.example.data.model.ExamQuestion
import com.example.data.model.ExamSection
import com.example.data.model.FlashcardItem
import com.example.data.model.GeneratedMockPaper
import com.example.data.model.MockPaperConfig
import com.example.data.model.QuizQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID

class AiStudyAssistantService {

    suspend fun generateMockPaper(config: MockPaperConfig): GeneratedMockPaper = withContext(Dispatchers.Default) {
        // Deterministic blueprint generation grounded in authentic syllabus
        delay(600) // Realistic AI synthesis feedback

        val sections = mutableListOf<ExamSection>()
        val markingScheme = mutableListOf<AnswerKeyItem>()
        var questionCounter = 1

        val isGseb = config.board.contains("GSEB", ignoreCase = true)
        val isMath = config.subject.contains("Math", ignoreCase = true)

        // Section A: Objective / Multiple Choice Questions
        if (config.includeMCQ) {
            val secAMarks = if (config.totalMarks <= 25) 5 else if (config.totalMarks <= 50) 10 else 16
            val questionsA = mutableListOf<ExamQuestion>()

            if (isMath) {
                questionsA.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "If HCF(306, 657) = 9, find the LCM(306, 657).",
                        questionType = "MCQ",
                        marks = 1,
                        options = listOf("22338", "22340", "18240", "24500"),
                        chapterHint = "Real Numbers"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "22338 (Option A)",
                        stepByStepMarking = "Formula: LCM = (a × b) / HCF = (306 × 657) / 9 = 22,338.",
                        keyFormulas = listOf("HCF(a, b) × LCM(a, b) = a × b")
                    )
                )

                questionsA.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "The discriminant D of quadratic equation 2x² - 4x + 3 = 0 is:",
                        questionType = "MCQ",
                        marks = 1,
                        options = listOf("-8 (No real roots)", "8 (Two real roots)", "0 (Equal roots)", "16"),
                        chapterHint = "Quadratic Equations"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "-8 (No real roots)",
                        stepByStepMarking = "D = b² - 4ac = (-4)² - 4(2)(3) = 16 - 24 = -8. Since D < 0, no real roots exist.",
                        keyFormulas = listOf("D = b² - 4ac")
                    )
                )

                questionsA.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "If sin θ = 3/5, what is the value of cot θ?",
                        questionType = "MCQ",
                        marks = 1,
                        options = listOf("4/3", "3/4", "5/4", "4/5"),
                        chapterHint = "Trigonometry"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "4/3",
                        stepByStepMarking = "Opposite = 3, Hypotenuse = 5. Adjacent = √(5² - 3²) = 4. cot θ = Adj/Opp = 4/3.",
                        keyFormulas = listOf("cot θ = cos θ / sin θ")
                    )
                )
            } else {
                questionsA.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "Which of the following is an exothermic combination reaction?",
                        questionType = "MCQ",
                        marks = 1,
                        options = listOf(
                            "CaO(s) + H2O(l) -> Ca(OH)2(aq) + Heat",
                            "2FeSO4(s) -> Fe2O3(s) + SO2(g) + SO3(g)",
                            "CaCO3(s) -> CaO(s) + CO2(g)",
                            "2Pb(NO3)2(s) -> 2PbO(s) + 4NO2(g) + O2(g)"
                        ),
                        chapterHint = "Chemical Reactions"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "CaO + H2O -> Ca(OH)2 + Heat",
                        stepByStepMarking = "Slaking of lime releases significant thermal energy, making it exothermic combination.",
                        keyFormulas = emptyList()
                    )
                )
            }

            val allocatedSecA = questionsA.sumOf { it.marks }
            sections.add(
                ExamSection(
                    sectionName = "Section A (Objective & Conceptual)",
                    instructions = "Answer all questions. Each carries 1 mark. Select the single correct option.",
                    totalMarks = allocatedSecA,
                    questions = questionsA
                )
            )
        }

        // Section B: Short Answer Questions
        if (config.includeShortAnswer) {
            val questionsB = mutableListOf<ExamQuestion>()

            if (isMath) {
                questionsB.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "Prove by contradiction that √5 is an irrational number.",
                        questionType = "SA",
                        marks = 3,
                        options = null,
                        chapterHint = "Real Numbers"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "Proof by contradiction showing 5 divides both a and b, contradicting co-prime condition.",
                        stepByStepMarking = "[1M] State assumption √5 = a/b with co-prime a, b. [1M] Show a² = 5b² hence 5 divides a. [1M] Substitute a = 5c, show 5 divides b, concluding contradiction.",
                        keyFormulas = listOf("a = 5c => b² = 5c²")
                    )
                )

                questionsB.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "Find the zeroes of the quadratic polynomial p(x) = x² - 2x - 8, and verify the relationship between zeroes and coefficients.",
                        questionType = "SA",
                        marks = 3,
                        options = null,
                        chapterHint = "Polynomials"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "Zeroes are 4 and -2. Sum = 2, Product = -8.",
                        stepByStepMarking = "[1M] Factorize: (x - 4)(x + 2) = 0. [1M] Zeroes: α = 4, β = -2. [1M] Verify α + β = -(-2)/1 = 2 and αβ = -8/1 = -8.",
                        keyFormulas = listOf("α + β = -b/a", "αβ = c/a")
                    )
                )
            } else {
                questionsB.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "State Snell's Law of refraction and define refractive index of a medium.",
                        questionType = "SA",
                        marks = 3,
                        options = null,
                        chapterHint = "Light: Refraction"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "sin i / sin r = constant (n21). Refractive index n = c / v.",
                        stepByStepMarking = "[1.5M] State Snell's law formula sin i / sin r = n21. [1.5M] Definition of refractive index as ratio of speed of light in vacuum to medium.",
                        keyFormulas = listOf("n = c / v", "n21 = sin i / sin r")
                    )
                )
            }

            val allocatedSecB = questionsB.sumOf { it.marks }
            sections.add(
                ExamSection(
                    sectionName = "Section B (Short Answer Problems)",
                    instructions = "Show complete mathematical/scientific steps. Each question carries marks as indicated.",
                    totalMarks = allocatedSecB,
                    questions = questionsB
                )
            )
        }

        // Section C: Long Answer / Analytical
        if (config.includeLongAnswer) {
            val questionsC = mutableListOf<ExamQuestion>()

            if (isMath) {
                questionsC.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "State and prove Basic Proportionality Theorem (Thales Theorem).",
                        questionType = "LA",
                        marks = 4,
                        options = null,
                        chapterHint = "Triangles"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "Statement + Geometric Construction + Ratio of areas proof leading to AD/DB = AE/EC.",
                        stepByStepMarking = "[1M] Accurate statement & triangle figure. [1M] Construction of altitudes EN and DM. [2M] Proof equating area ratios: ar(ADE)/ar(BDE) = ar(ADE)/ar(DEC).",
                        keyFormulas = listOf("Area of triangle = 1/2 × base × height")
                    )
                )
            } else {
                questionsC.add(
                    ExamQuestion(
                        questionNumber = questionCounter++,
                        prompt = "Draw a ray diagram for image formation by a concave mirror when the object is placed between Focus (F) and Centre of Curvature (C). State image nature, position, and size.",
                        questionType = "LA",
                        marks = 4,
                        options = null,
                        chapterHint = "Light: Reflection"
                    )
                )
                markingScheme.add(
                    AnswerKeyItem(
                        questionNumber = questionCounter - 1,
                        expectedAnswer = "Image formed beyond C, Real, Inverted, and Magnified.",
                        stepByStepMarking = "[2M] Correct labeled ray diagram with arrows. [2M] Nature: Real & Inverted, Position: Beyond C, Size: Magnified.",
                        keyFormulas = emptyList()
                    )
                )
            }

            val allocatedSecC = questionsC.sumOf { it.marks }
            sections.add(
                ExamSection(
                    sectionName = "Section C (Analytical & Descriptive Questions)",
                    instructions = "Provide detailed derivations or proofs with clean diagrams.",
                    totalMarks = allocatedSecC,
                    questions = questionsC
                )
            )
        }

        val totalCalculatedMarks = sections.sumOf { it.totalMarks }

        GeneratedMockPaper(
            id = UUID.randomUUID().toString(),
            title = "${config.board} ${config.classLevel} ${config.subject} Mock Paper",
            board = config.board,
            classLevel = config.classLevel,
            subject = config.subject,
            language = config.language,
            totalMarks = totalCalculatedMarks,
            durationMinutes = config.durationMinutes,
            difficulty = config.difficulty,
            generalInstructions = listOf(
                "All questions are compulsory. Marks are indicated against each question.",
                "Read each question carefully before attempting.",
                "Use of scientific calculators is strictly prohibited as per board rules.",
                "Figures in the right margin indicate full marks."
            ),
            sections = sections,
            markingScheme = markingScheme
        )
    }

    suspend fun generateFlashcards(topic: String, count: Int = 4): List<FlashcardItem> = withContext(Dispatchers.Default) {
        delay(400)
        listOf(
            FlashcardItem(
                id = UUID.randomUUID().toString(),
                front = "What is the core principle of '$topic'?",
                back = "In $topic, foundational rules dictate that variables and relationships preserve conservation laws and structural symmetries.",
                hint = "Think about the primary definition and underlying formula",
                keyConcept = topic
            ),
            FlashcardItem(
                id = UUID.randomUUID().toString(),
                front = "Key formula / equation associated with '$topic'?",
                back = "Mathematical representation defines the rate of change or equilibrium constant specifically applicable to $topic problems.",
                hint = "Look for standard symbols (e.g. F, E, Δ, or λ)",
                keyConcept = topic
            ),
            FlashcardItem(
                id = UUID.randomUUID().toString(),
                front = "Common board exam pitfall in '$topic'?",
                back = "Students frequently forget unit conversions (e.g. converting cm to meters or grams to kilograms) or sign conventions.",
                hint = "Check signs and SI units",
                keyConcept = topic
            )
        ).take(count)
    }

    suspend fun generateQuizQuestions(topic: String): List<QuizQuestion> = withContext(Dispatchers.Default) {
        delay(400)
        listOf(
            QuizQuestion(
                id = UUID.randomUUID().toString(),
                question = "In the study of '$topic', which statement is strictly correct?",
                options = listOf(
                    "The fundamental relation holds universally under standard conditions.",
                    "The quantity depends inversely on arbitrary coordinate systems.",
                    "No energy transfer occurs regardless of temperature gradients.",
                    "It violates conservation principles in closed thermodynamic loops."
                ),
                correctIndex = 0,
                explanation = "Standard board principles state that fundamental properties remain invariant under defined reference conditions.",
                chapter = topic,
                marks = 1
            ),
            QuizQuestion(
                id = UUID.randomUUID().toString(),
                question = "When solving examination problems on '$topic', what is the first priority step?",
                options = listOf(
                    "Identify known variables and write standard governing formula.",
                    "Guess numerical approximations without checking units.",
                    "Ignore boundary conditions given in the prompt.",
                    "Directly multiply arbitrary values."
                ),
                correctIndex = 0,
                explanation = "GSEB & CBSE marking schemes reward stating the formula and writing given data with appropriate units.",
                chapter = topic,
                marks = 1
            )
        )
    }

    suspend fun askAssistant(prompt: String, subject: String = "General"): String = withContext(Dispatchers.Default) {
        delay(500)
        """💡 Academic Guidance for '$prompt':

1. Core Definition: In $subject, this topic addresses fundamental principles frequently tested in annual board examinations.
2. Step-by-Step Method:
   - Identify given terms, constants, and target unknowns.
   - Apply the governing equation or rule.
   - Verify unit consistency (SI Units) and review for edge cases.
3. Exam Tip: Write clear diagrams with arrows and state formulas explicitly to secure full step marks."""
    }
}
