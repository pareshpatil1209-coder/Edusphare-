package com.example.feature.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AiStudyAssistantService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun askAssistant(prompt: String, subjectContext: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateLocalEducationalResponse(prompt, subjectContext)
        }

        try {
            val systemInstruction = "You are EduSphere AI, a polite, encouraging, and pedagogically sound academic tutor for secondary and higher secondary school students. Explain concepts step-by-step, use clear analogies, and provide short summary bullet points."

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val contextText = if (!subjectContext.isNullOrBlank()) "Context: $subjectContext\n\n" else ""
                            put(JSONObject().apply { put("text", "$contextText$prompt") })
                        }
                        put("parts", partsArray)
                    })
                }
                put("contents", contentsArray)

                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", systemInstruction) })
                    })
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)

            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseText = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext generateLocalEducationalResponse(prompt, subjectContext)
            }

            val jsonResponse = JSONObject(responseText)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val answerText = parts?.optJSONObject(0)?.optString("text")

            if (!answerText.isNullOrBlank()) {
                answerText
            } else {
                generateLocalEducationalResponse(prompt, subjectContext)
            }
        } catch (e: Exception) {
            generateLocalEducationalResponse(prompt, subjectContext)
        }
    }

    private fun generateLocalEducationalResponse(prompt: String, subject: String?): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("simplify") || lower.contains("simple") -> {
                "Here is a simplified explanation:\n\n• Think of this concept like water flowing in a pipe: pressure is like voltage, the rate of flow is current, and any narrowing in the pipe is electrical resistance.\n• Key Takeaway: Greater resistance means lower current unless you increase voltage."
            }
            lower.contains("summary") || lower.contains("summarize") -> {
                "📋 Quick Chapter Summary:\n\n1. Core Principle: Conservation laws apply to both energy and mass in isolated systems.\n2. Key Formula: Memorize the fundamental relationships and SI units.\n3. Common Exam Pitfall: Be careful with unit conversions (e.g. converting cm to meters and minutes to seconds).\n4. Practice Recommendation: Solve at least 3 numerical problems from past board papers."
            }
            lower.contains("question") || lower.contains("quiz") -> {
                "📝 Practice Question for You:\n\nQuestion: A 12V battery is connected across an unknown resistor. There is a current of 2.5 mA in the circuit. Find the value of resistance.\n\nHint: Use Ohm's Law (R = V / I), and convert mA to Amperes (2.5 mA = 2.5 × 10⁻³ A).\n\nAnswer: R = 12 / (2.5 × 10⁻³) = 4800 Ω = 4.8 kΩ."
            }
            lower.contains("plan") || lower.contains("revision") -> {
                "📅 7-Day Board Exam Revision Blueprint:\n\n• Day 1-2: Review high-weightage chapters and create one-page formula sheets.\n• Day 3-4: Solve chapter-wise multiple choice and short numerical questions.\n• Day 5: Attempt a full timed 3-hour model examination.\n• Day 6: Analyze mistakes, revise weak topics, and practice diagram labeling.\n• Day 7: Light revision of flashcards and rest well before the exam."
            }
            else -> {
                "💡 Academic Explanation for '${prompt}':\n\n1. Concept Definition: In $subject, this topic forms the cornerstone for higher secondary applications.\n2. Fundamental Rule: Always identify the given variables first and write the standard formula before solving.\n3. Real-world Example: Engineers and scientists utilize this exact principle to design circuits, structures, and algorithms.\n\nTip: You can tap the speaker icon above to listen to this answer with Text-to-Speech!"
            }
        }
    }

    suspend fun generateMockPaper(config: com.example.data.model.MockPaperConfig): com.example.data.model.GeneratedMockPaper = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext com.example.data.model.AuthorizedEducationalCatalog.generateStandardMockPaper(config)
        }

        try {
            val prompt = """
                Generate a formal academic mock examination paper for ${config.board} ${config.classLevel} ${config.subject}.
                Chapter scope: ${config.chapter}. Language: ${config.language}. Difficulty: ${config.difficulty}. Total Marks: ${config.totalMarks}.
                Include 4 Sections:
                Section A: 4 MCQs/Objective questions (1 mark each).
                Section B: 2 Short answer questions (2 marks each).
                Section C: 1 Long analytical question (3 marks).
                Section D: 1 Comprehensive theorem/problem question (5 marks).
                For each question, provide:
                - question text
                - options array (for Section A)
                - exact marks
                - full step-by-step solution / answer key
                - marking scheme breakdown
                Respond in valid JSON with structure:
                {
                   "generalInstructions": ["Instruction 1", "Instruction 2"],
                   "sectionA": [{"question": "...", "options": ["A", "B", "C", "D"], "marks": 1, "solution": "...", "scheme": "..."}],
                   "sectionB": [{"question": "...", "marks": 2, "solution": "...", "scheme": "..."}],
                   "sectionC": [{"question": "...", "marks": 3, "solution": "...", "scheme": "..."}],
                   "sectionD": [{"question": "...", "marks": 5, "solution": "...", "scheme": "..."}]
                }
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder().url(url).post(requestBody).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext com.example.data.model.AuthorizedEducationalCatalog.generateStandardMockPaper(config)
            }

            val respText = response.body?.string() ?: ""
            val jsonResp = JSONObject(respText)
            val candidate = jsonResp.optJSONArray("candidates")?.optJSONObject(0)
            val text = candidate?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

            if (text.isBlank()) {
                return@withContext com.example.data.model.AuthorizedEducationalCatalog.generateStandardMockPaper(config)
            }

            val parsedJson = JSONObject(text)
            val instructions = mutableListOf<String>()
            val instArray = parsedJson.optJSONArray("generalInstructions")
            if (instArray != null) {
                for (i in 0 until instArray.length()) {
                    instructions.add(instArray.optString(i))
                }
            } else {
                instructions.addAll(listOf(
                    "Duration: ${config.durationMinutes} Minutes.",
                    "All sections are compulsory with internal choice where indicated.",
                    "Calculators are not permitted in board examinations."
                ))
            }

            fun parseQuestions(arrayName: String, secName: String, defaultMarks: Int, startNum: Int): List<com.example.data.model.MockPaperQuestion> {
                val list = mutableListOf<com.example.data.model.MockPaperQuestion>()
                val arr = parsedJson.optJSONArray(arrayName) ?: return list
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val opts = mutableListOf<String>()
                    val optArr = obj.optJSONArray("options")
                    if (optArr != null) {
                        for (j in 0 until optArr.length()) opts.add(optArr.optString(j))
                    }
                    list.add(
                        com.example.data.model.MockPaperQuestion(
                            number = startNum + i,
                            section = secName,
                            question = obj.optString("question", "Question ${startNum + i}"),
                            options = if (opts.isNotEmpty()) opts else null,
                            marks = obj.optInt("marks", defaultMarks),
                            answerKeyOrSolution = obj.optString("solution", "Solution available in marking guide."),
                            markingSchemeStep = obj.optString("scheme", "$defaultMarks marks for complete step working.")
                        )
                    )
                }
                return list
            }

            val secA = parseQuestions("sectionA", "Section A", 1, 1)
            val secB = parseQuestions("sectionB", "Section B", 2, secA.size + 1)
            val secC = parseQuestions("sectionC", "Section C", 3, secA.size + secB.size + 1)
            val secD = parseQuestions("sectionD", "Section D", 5, secA.size + secB.size + secC.size + 1)

            if (secA.isEmpty() && secB.isEmpty()) {
                return@withContext com.example.data.model.AuthorizedEducationalCatalog.generateStandardMockPaper(config)
            }

            val sections = listOf(
                com.example.data.model.MockPaperSection(
                    name = "Section A",
                    title = "Objective & MCQs",
                    marksPerQuestion = 1,
                    totalSectionMarks = secA.sumOf { it.marks },
                    instructions = "Choose the correct option.",
                    questions = secA
                ),
                com.example.data.model.MockPaperSection(
                    name = "Section B",
                    title = "Short Answer Questions",
                    marksPerQuestion = 2,
                    totalSectionMarks = secB.sumOf { it.marks },
                    instructions = "Answer in 30-50 words.",
                    questions = secB
                ),
                com.example.data.model.MockPaperSection(
                    name = "Section C",
                    title = "Long Analytical Questions",
                    marksPerQuestion = 3,
                    totalSectionMarks = secC.sumOf { it.marks },
                    instructions = "Answer in 60-80 words with necessary steps.",
                    questions = secC
                ),
                com.example.data.model.MockPaperSection(
                    name = "Section D",
                    title = "Theorems & Comprehensive Problems",
                    marksPerQuestion = 5,
                    totalSectionMarks = secD.sumOf { it.marks },
                    instructions = "State theorem, draw proper figures, and provide full proof.",
                    questions = secD
                )
            )

            com.example.data.model.GeneratedMockPaper(
                id = "mock_${System.currentTimeMillis()}",
                title = "${config.board} ${config.classLevel} ${config.subject} Mock Paper",
                board = config.board,
                classLevel = config.classLevel,
                subject = config.subject,
                totalMarks = sections.sumOf { it.totalSectionMarks },
                durationMinutes = config.durationMinutes,
                generalInstructions = instructions,
                sections = sections
            )
        } catch (e: Exception) {
            com.example.data.model.AuthorizedEducationalCatalog.generateStandardMockPaper(config)
        }
    }

    suspend fun generateFlashcards(topicOrNotes: String): List<com.example.data.model.FlashcardItem> = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext listOf(
                com.example.data.model.FlashcardItem(
                    id = "fc_${System.currentTimeMillis()}_1",
                    frontQuestion = "Core Definition: $topicOrNotes",
                    backAnswer = "Fundamental rule and application in academic curriculum with standard SI units and formula.",
                    category = "Key Concept"
                ),
                com.example.data.model.FlashcardItem(
                    id = "fc_${System.currentTimeMillis()}_2",
                    frontQuestion = "Important Formula or Condition for $topicOrNotes",
                    backAnswer = "Formula: Standard relation under equilibrium / steady state condition.",
                    category = "Formula"
                ),
                com.example.data.model.FlashcardItem(
                    id = "fc_${System.currentTimeMillis()}_3",
                    frontQuestion = "Common Board Exam Pitfall in $topicOrNotes",
                    backAnswer = "Always check unit consistency (convert cm to meters and minutes to seconds) and write final SI units.",
                    category = "Exam Tip"
                )
            )
        }

        try {
            val prompt = """
                Generate 5 high-yield revision flashcards for the student topic/notes: "$topicOrNotes".
                Respond in valid JSON array:
                [
                  {"front": "Question / Concept", "back": "Clear concise answer / formula", "category": "Concept/Formula/Definition", "hint": "Short clue"}
                ]
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder().url(url).post(requestBody).build()
            val response = client.newCall(request).execute()

            val respText = response.body?.string() ?: ""
            val jsonResp = JSONObject(respText)
            val candidate = jsonResp.optJSONArray("candidates")?.optJSONObject(0)
            val text = candidate?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text") ?: ""

            val arr = JSONArray(text)
            val list = mutableListOf<com.example.data.model.FlashcardItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                list.add(
                    com.example.data.model.FlashcardItem(
                        id = "fc_gen_${System.currentTimeMillis()}_$i",
                        frontQuestion = obj.optString("front", "Concept $i"),
                        backAnswer = obj.optString("back", "Explanation"),
                        category = obj.optString("category", "Revision"),
                        hint = obj.optString("hint").takeIf { it.isNotBlank() }
                    )
                )
            }
            if (list.isNotEmpty()) list else throw Exception("Empty list")
        } catch (e: Exception) {
            listOf(
                com.example.data.model.FlashcardItem(
                    id = "fc_fb_1",
                    frontQuestion = "Core Law / Definition: $topicOrNotes",
                    backAnswer = "Defines the fundamental mechanism and relationship between physical or mathematical quantities.",
                    category = "Concept"
                ),
                com.example.data.model.FlashcardItem(
                    id = "fc_fb_2",
                    frontQuestion = "Practical Application & Solved Example",
                    backAnswer = "Apply the governing theorem step-by-step; verify dimensions and boundary conditions.",
                    category = "Application"
                )
            )
        }
    }
}
