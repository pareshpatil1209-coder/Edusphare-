package com.example.data.model

data class EducationBoard(
    val id: String,
    val name: String,
    val fullName: String,
    val description: String,
    val officialPortal: String
)

data class GradeLevel(
    val id: String,
    val name: String,
    val boardId: String,
    val description: String
)

data class SubjectItem(
    val id: String,
    val name: String,
    val regionalName: String,
    val board: String,
    val classLevel: String,
    val colorHex: Long,
    val iconName: String,
    val totalChapters: Int
)

data class ChapterItem(
    val id: String,
    val subjectId: String,
    val number: Int,
    val title: String,
    val regionalTitle: String,
    val summary: String,
    val topics: List<String>,
    val officialSourceUrl: String? = null
)

data class LanguageCourse(
    val id: String,
    val title: String,
    val language: String,
    val script: String,
    val level: String,
    val description: String,
    val lessons: List<LanguageLesson>
)

data class LanguageLesson(
    val id: String,
    val title: String,
    val unit: String,
    val grammarRule: String,
    val vocabulary: List<VocabularyItem>,
    val exampleSentences: List<ExampleSentence>,
    val exercise: LessonExercise
)

data class VocabularyItem(
    val word: String,
    val phonetic: String,
    val meaning: String,
    val partOfSpeech: String
)

data class ExampleSentence(
    val original: String,
    val translated: String
)

data class LessonExercise(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class ComputerCourse(
    val id: String,
    val title: String,
    val category: String, // "Fundamentals", "Web", "Programming", "Data"
    val difficulty: String,
    val description: String,
    val lessons: List<ComputerLesson>
)

data class ComputerLesson(
    val id: String,
    val title: String,
    val module: String,
    val explanation: String,
    val codeSnippet: String,
    val language: String,
    val simulatedOutput: String,
    val exercisePrompt: String
)

object EducationalSeedData {

    val boards = listOf(
        EducationBoard(
            id = "GSEB",
            name = "GSEB",
            fullName = "Gujarat Secondary and Higher Secondary Education Board",
            description = "Official curriculum for Gujarat State Schools (Classes 9–12)",
            officialPortal = "https://gseb.org"
        ),
        EducationBoard(
            id = "CBSE",
            name = "CBSE",
            fullName = "Central Board of Secondary Education",
            description = "National curriculum following NCERT guidelines",
            officialPortal = "https://cbse.gov.in"
        ),
        EducationBoard(
            id = "ICSE",
            name = "ICSE / ISC",
            fullName = "Council for the Indian School Certificate Examinations",
            description = "Comprehensive standard Indian school certificate",
            officialPortal = "https://cisce.org"
        )
    )

    val classes = listOf("Class 9", "Class 10", "Class 11 Science", "Class 12 Science", "Class 11 Commerce", "Class 12 Commerce")

    val subjects = listOf(
        SubjectItem("math", "Mathematics", "ગણિત", "GSEB", "Class 10", 0xFF1E3A8A, "Calculate", 15),
        SubjectItem("science", "Science & Tech", "વિજ્ઞાન અને ટેકનોલોજી", "GSEB", "Class 10", 0xFF059669, "Bolt", 16),
        SubjectItem("social", "Social Science", "સામાજિક વિજ્ઞાન", "GSEB", "Class 10", 0xFFD97706, "Public", 21),
        SubjectItem("physics", "Physics", "ભૌતિક વિજ્ઞાન", "GSEB", "Class 12", 0xFF2563EB, "Bolt", 14),
        SubjectItem("chemistry", "Chemistry", "રસાયણ વિજ્ઞાન", "GSEB", "Class 12", 0xFF7C3AED, "Science", 16),
        SubjectItem("biology", "Biology", "જીવ વિજ્ઞાન", "GSEB", "Class 12", 0xFF059669, "Eco", 16),
        SubjectItem("english", "English FL/SL", "અંગ્રેજી", "GSEB", "Class 10", 0xFF0284C7, "Translate", 10),
        SubjectItem("gujarati", "Gujarati FL/SL", "ગુજરાતી", "GSEB", "Class 10", 0xFFDC2626, "MenuBook", 12),
        SubjectItem("computer", "Computer Studies", "કોમ્પ્યુટર અધ્યયન", "GSEB", "Class 10", 0xFF4F46E5, "Code", 13)
    )

    val chapters = listOf(
        ChapterItem(
            id = "math_ch1",
            subjectId = "math",
            number = 1,
            title = "Real Numbers",
            regionalTitle = "વાસ્તવિક સંખ્યાઓ",
            summary = "Fundamental Theorem of Arithmetic, Euclid's Division Lemma, proving irrationality of √2, √3, √5, decimal expansions.",
            topics = listOf("Fundamental Theorem of Arithmetic", "Revisiting Irrational Numbers", "Prime Factorization Method", "HCF & LCM Applications"),
            officialSourceUrl = "https://ncert.nic.in/textbook.php"
        ),
        ChapterItem(
            id = "math_ch2",
            subjectId = "math",
            number = 2,
            title = "Polynomials",
            regionalTitle = "બહુપદીઓ",
            summary = "Geometrical meaning of zeroes, relationship between zeroes and coefficients of quadratic and cubic polynomials.",
            topics = listOf("Geometric representation of Zeroes", "Relationship between Zeroes and Coefficients", "Division Algorithm for Polynomials"),
            officialSourceUrl = "https://ncert.nic.in/textbook.php"
        ),
        ChapterItem(
            id = "math_ch3",
            subjectId = "math",
            number = 3,
            title = "Pair of Linear Equations in Two Variables",
            regionalTitle = "દ્વિચલ સુરેખ સમીકરણયુગ્મ",
            summary = "Graphical and algebraic methods: substitution, elimination, and cross-multiplication with practical word problems.",
            topics = listOf("Graphical Solution", "Substitution Method", "Elimination Method", "Consistency Conditions"),
            officialSourceUrl = "https://ncert.nic.in/textbook.php"
        ),
        ChapterItem(
            id = "science_ch1",
            subjectId = "science",
            number = 1,
            title = "Chemical Reactions & Equations",
            regionalTitle = "રાસાયણિક પ્રક્રિયાઓ અને સમીકરણો",
            summary = "Balancing chemical equations, combination, decomposition, displacement, double displacement, oxidation and reduction.",
            topics = listOf("Writing & Balancing Chemical Equations", "Types of Chemical Reactions", "Corrosion and Rancidity", "Exothermic and Endothermic Reactions"),
            officialSourceUrl = "https://diksha.gov.in"
        ),
        ChapterItem(
            id = "science_ch10",
            subjectId = "science",
            number = 10,
            title = "Light: Reflection & Refraction",
            regionalTitle = "પ્રકાશ - પરાવર્તન અને વક્રીભવન",
            summary = "Spherical mirrors, mirror formula, magnification, refraction of light, Snell's law, refractive index, lens formula.",
            topics = listOf("Reflection by Spherical Mirrors", "Mirror Formula & Magnification", "Laws of Refraction & Snell's Law", "Power of Lens"),
            officialSourceUrl = "https://diksha.gov.in"
        ),
        ChapterItem(
            id = "physics_ch1",
            subjectId = "physics",
            number = 1,
            title = "Electric Charges & Fields",
            regionalTitle = "વિદ્યુતભાર અને ક્ષેત્રો",
            summary = "Coulomb's Law, forces between multiple charges, electric field lines, electric dipole, torque on a dipole, Gauss's Theorem applications.",
            topics = listOf("Coulomb's Law in Vector Form", "Electric Dipole & Dipole Moment", "Gauss's Law and Flux", "Applications of Gauss's Law"),
            officialSourceUrl = "https://ncert.nic.in"
        )
    )

    val languageCourses = listOf(
        LanguageCourse(
            id = "lang_gujarati",
            title = "Structured Gujarati (ગુજરાતી અધ્યયન)",
            language = "Gujarati",
            script = "Gujarati",
            level = "Beginner to Advanced (Board Preparatory)",
            description = "Master Gujarati grammar (વ્યાકરણ), idioms (રૂઢિપ્રયોગો), sandhi, and vocabulary for GSEB examination excellence.",
            lessons = listOf(
                LanguageLesson(
                    id = "guj_l1",
                    title = "Gujarati Alphabets & Swar-Vyanjan (સ્વર અને વ્યંજન)",
                    unit = "Unit 1: Fundamentals",
                    grammarRule = "Gujarati has 13 vowels (સ્વર) like અ, આ, ઇ, ઈ, ઉ, ઊ and 34 consonants (વ્યંજન) from ક to જ્ઞ.",
                    vocabulary = listOf(
                        VocabularyItem("નમસ્તે", "Namaste", "Greetings / Hello", "Interjection"),
                        VocabularyItem("વિદ્યાર્થી", "Vidyarthi", "Student", "Noun"),
                        VocabularyItem("શિક્ષક", "Shikshak", "Teacher", "Noun"),
                        VocabularyItem("પુસ્તક", "Pustak", "Book", "Noun")
                    ),
                    exampleSentences = listOf(
                        ExampleSentence("હું દરરોજ અભ્યાસ કરું છું.", "I study every day."),
                        ExampleSentence("આ પુસ્તક ખૂબ ઉપયોગી છે.", "This book is very useful.")
                    ),
                    exercise = LessonExercise(
                        prompt = "Identify the Gujarati word for 'Student':",
                        options = listOf("શિક્ષક", "વિદ્યાર્થી", "પુસ્તક", "શાળા"),
                        correctIndex = 1,
                        explanation = "વિદ્યાર્થી (Vidyarthi) means Student in Gujarati."
                    )
                ),
                LanguageLesson(
                    id = "guj_l2",
                    title = "Grammar: Sandhi & Samas (સંધિ અને સમાસ)",
                    unit = "Unit 2: Board Vyakarana",
                    grammarRule = "સંધિ (Sandhi) joins two sounds, e.g., સૂર્ય + ઉદય = સૂર્યોદય. દ્વન્દ્વ સમાસ joins equal words, e.g., ભાઈ-બહેન.",
                    vocabulary = listOf(
                        VocabularyItem("સૂર્યોદય", "Suryoday", "Sunrise (સૂર્ય + ઉદય)", "Noun"),
                        VocabularyItem("વિદ્યાલય", "Vidyalaya", "School (વિદ્યા + આલય)", "Noun"),
                        VocabularyItem("માતા-પિતા", "Mata-Pita", "Parents (Dvandva Samas)", "Noun")
                    ),
                    exampleSentences = listOf(
                        ExampleSentence("સૂર્યોદય સમયે પ્રકૃતિ સુંદર લાગે છે.", "Nature looks beautiful during sunrise."),
                        ExampleSentence("વિદ્યાર્થીઓ વિદ્યાલયે જાય છે.", "Students are going to school.")
                    ),
                    exercise = LessonExercise(
                        prompt = "What is the correct Sandhi of 'વિદ્યા + આલય'?",
                        options = listOf("વિદ્યાલય", "વિદ્યાઆલય", "વિદ્યોલય", "વિદ્યાલ"),
                        correctIndex = 0,
                        explanation = "વિદ્યા + આલય combines as દીર્ઘ સંધિ to form વિદ્યાલય."
                    )
                )
            )
        ),
        LanguageCourse(
            id = "lang_english",
            title = "Academic English Grammar & Composition",
            language = "English",
            script = "Latin",
            level = "Secondary & Higher Secondary",
            description = "Comprehensive mastery of Tenses, Voice, Reported Speech, Academic Vocabulary, and Notice/Report Writing.",
            lessons = listOf(
                LanguageLesson(
                    id = "eng_l1",
                    title = "Active & Passive Voice Rules",
                    unit = "Unit 1: Syntax Mastery",
                    grammarRule = "Object + auxiliary verb (to be) + past participle (V3) + by + Subject. Example: 'The teacher praised the student' becomes 'The student was praised by the teacher'.",
                    vocabulary = listOf(
                        VocabularyItem("Articulate", "/ɑːrˈtɪk.jə.lət/", "Express idea fluently and coherently", "Verb/Adj"),
                        VocabularyItem("Comprehensive", "/ˌkɑːm.prɪˈhen.sɪv/", "Complete, including all elements", "Adjective"),
                        VocabularyItem("Hypothesis", "/haɪˈpɑː.θə.sɪs/", "A proposed explanation on limited evidence", "Noun")
                    ),
                    exampleSentences = listOf(
                        ExampleSentence("The board published the official syllabus yesterday.", "The official syllabus was published by the board yesterday."),
                        ExampleSentence("Students are writing their mock examination.", "The mock examination is being written by the students.")
                    ),
                    exercise = LessonExercise(
                        prompt = "Convert to Passive: 'The scientist discovered a new element.'",
                        options = listOf(
                            "A new element discovered the scientist.",
                            "A new element was discovered by the scientist.",
                            "A new element is being discovered by the scientist.",
                            "The scientist had been discovered."
                        ),
                        correctIndex = 1,
                        explanation = "Past Simple passive requires 'was/were + past participle (discovered)'."
                    )
                )
            )
        ),
        LanguageCourse(
            id = "lang_hindi",
            title = "Hindi Vyakaran & Sahitya (हिन्दी व्याकरण)",
            language = "Hindi",
            script = "Devanagari",
            level = "Classes 9–12 Standard",
            description = "Detailed coverage of Sandhi, Ras, Chhand, Alankar, Muhavare, and formal letter writing.",
            lessons = listOf(
                LanguageLesson(
                    id = "hin_l1",
                    title = "Alankar Parichay (अलंकार: अनुप्रास व उपमा)",
                    unit = "Unit 1: Kavyashastra",
                    grammarRule = "अनुप्रास अलंकार में वर्णों की आवृत्ति होती है (जैसे तरनि तनूजा तट तमाल)। उपमा अलंकार में सादृश्यता दिखाई जाती है।",
                    vocabulary = listOf(
                        VocabularyItem("परिश्रम", "Parishram", "Hard work / Diligence", "Noun"),
                        VocabularyItem("सफलता", "Safalta", "Success", "Noun"),
                        VocabularyItem("अनुशासन", "Anushasan", "Discipline", "Noun")
                    ),
                    exampleSentences = listOf(
                        ExampleSentence("परिश्रम ही सफलता की कुंजी है।", "Hard work is the key to success."),
                        ExampleSentence("विद्यार्थी को अनुशासित रहना चाहिए।", "A student must remain disciplined.")
                    ),
                    exercise = LessonExercise(
                        prompt = "'चारु चंद्र की चंचल किरणें' में कौन-सा अलंकार है?",
                        options = listOf("उपमा अलंकार", "अनुप्रास अलंकार", "रूपक अलंकार", "यमक अलंकार"),
                        correctIndex = 1,
                        explanation = "'च' वर्ण की बार-बार आवृत्ति के कारण यहाँ अनुप्रास अलंकार है।"
                    )
                )
            )
        )
    )

    val computerCourses = listOf(
        ComputerCourse(
            id = "cs_python",
            title = "Python Programming for Students",
            category = "Programming",
            difficulty = "Beginner to Intermediate",
            description = "Learn variables, loops, functions, lists, dictionaries, and problem solving used in CBSE/State Board CS curriculum.",
            lessons = listOf(
                ComputerLesson(
                    id = "py_l1",
                    title = "Variables, Data Types & I/O",
                    module = "Module 1: Getting Started",
                    explanation = "Python is dynamically typed. Use print() to output data and input() to accept string input from the user.",
                    codeSnippet = """# Student Marksheet Calculator
name = "Dev"
math_marks = 95
science_marks = 88
total = math_marks + science_marks
percentage = total / 2.0

print(f"Student: {name}")
print(f"Total Marks: {total}/200")
print(f"Percentage: {percentage}%")""",
                    language = "python",
                    simulatedOutput = "Student: Dev\nTotal Marks: 183/200\nPercentage: 91.5%\n[Execution completed successfully in 0.04s]",
                    exercisePrompt = "Modify the variables to add 'english_marks = 92' and compute the new 3-subject percentage."
                ),
                ComputerLesson(
                    id = "py_l2",
                    title = "Loops & Control Flow (for / while)",
                    module = "Module 2: Logic Building",
                    explanation = "Use 'for i in range(start, stop, step)' to iterate. Use 'if-elif-else' for conditional decisions.",
                    codeSnippet = """# Print prime numbers up to 20
primes = []
for num in range(2, 21):
    is_prime = True
    for i in range(2, int(num ** 0.5) + 1):
        if num % i == 0:
            is_prime = False
            break
    if is_prime:
        primes.append(num)

print("Primes between 2 and 20:", primes)""",
                    language = "python",
                    simulatedOutput = "Primes between 2 and 20: [2, 3, 5, 7, 11, 13, 17, 19]\n[Execution completed successfully in 0.06s]",
                    exercisePrompt = "Write a loop to calculate the factorial of a given number n."
                )
            )
        ),
        ComputerCourse(
            id = "cs_web",
            title = "Web Development: HTML, CSS & JavaScript",
            category = "Web",
            difficulty = "Beginner",
            description = "Build responsive, modern web pages using semantic HTML5 tags, CSS3 flexbox/grid, and JavaScript event listeners.",
            lessons = listOf(
                ComputerLesson(
                    id = "web_l1",
                    title = "HTML5 Structure & Semantic Tags",
                    module = "Module 1: Document Structure",
                    explanation = "HTML5 introduces <header>, <nav>, <main>, <article>, and <footer> tags for accessible search-engine friendly design.",
                    codeSnippet = """<!DOCTYPE html>
<html>
<head>
    <title>My Study Portal</title>
</head>
<body>
    <header>
        <h1>EduSphere Learning Hub</h1>
    </header>
    <main>
        <p>Welcome to GSEB Class 10 Study Material portal.</p>
        <button onclick="alert('Happy Learning!')">Start Study</button>
    </main>
</body>
</html>""",
                    language = "html",
                    simulatedOutput = "<Rendered HTML DOM>\nHeader: 'EduSphere Learning Hub'\nBody: 'Welcome to GSEB Class 10 Study Material portal.'\nButton: [Start Study] (Interactive)\n[DOM Parse Tree Validated]",
                    exercisePrompt = "Add an ordered list <ol> containing three school subjects inside the <main> container."
                )
            )
        ),
        ComputerCourse(
            id = "cs_fundamentals",
            title = "Computer Fundamentals & Architecture",
            category = "Fundamentals",
            difficulty = "Beginner",
            description = "CPU registers, ALUs, memory hierarchy, cache, binary/hex conversions, operating system processes, and Boolean logic.",
            lessons = listOf(
                ComputerLesson(
                    id = "fund_l1",
                    title = "Binary, Octal & Hexadecimal Conversions",
                    module = "Module 1: Number Systems",
                    explanation = "Computers represent all instructions in base-2 (binary). Four binary bits (nibble) form one hexadecimal digit.",
                    codeSnippet = """# Number base demonstration
decimal_val = 42
binary_val = bin(decimal_val)
hex_val = hex(decimal_val)

print(f"Decimal: {decimal_val}")
print(f"Binary : {binary_val}")
print(f"Hex    : {hex_val}")""",
                    language = "python",
                    simulatedOutput = "Decimal: 42\nBinary : 0b101010\nHex    : 0x2a\n[Computed accurately across radix bases]",
                    exercisePrompt = "Convert decimal 255 to hexadecimal manually and check the output."
                )
            )
        )
    )
}
