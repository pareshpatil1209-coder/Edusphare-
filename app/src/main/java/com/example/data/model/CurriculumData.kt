package com.example.data.model

data class SubjectItem(
    val id: String,
    val name: String,
    val iconName: String,
    val colorHex: Long,
    val totalChapters: Int,
    val classLevels: List<String>,
    val boards: List<String>,
    val description: String
)

data class ChapterItem(
    val id: String,
    val subjectId: String,
    val number: Int,
    val title: String,
    val summary: String,
    val readingTimeMinutes: Int,
    val topics: List<String>,
    val keyFormulasOrNotes: List<String>,
    val content: String,
    val sampleQuestions: List<QuestionItem>
)

data class QuestionItem(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val difficulty: String = "Medium" // "Easy", "Medium", "Hard"
)

data class LanguageLesson(
    val id: String,
    val language: String, // "English", "Hindi", "Gujarati", "Sanskrit"
    val title: String,
    val category: String, // "Vocabulary", "Grammar", "Flashcards", "Reading"
    val level: String, // "Beginner", "Intermediate", "Advanced"
    val items: List<VocabularyCard>,
    val practiceQuiz: List<QuestionItem>
)

data class VocabularyCard(
    val word: String,
    val phonetic: String,
    val meaning: String,
    val exampleSentence: String,
    val translation: String
)

data class CodingCourse(
    val id: String,
    val title: String,
    val languageName: String, // "Python", "HTML", "CSS", "JavaScript", "C++", "Java"
    val iconSymbol: String,
    val description: String,
    val lessons: List<CodingLesson>
)

data class CodingLesson(
    val id: String,
    val title: String,
    val conceptExplanation: String,
    val codeSnippet: String,
    val expectedOutput: String,
    val challengePrompt: String,
    val solutionCode: String
)

object EducationalSeedData {

    val subjects = listOf(
        SubjectItem(
            id = "math",
            name = "Mathematics",
            iconName = "Calculate",
            colorHex = 0xFF4F46E5,
            totalChapters = 14,
            classLevels = listOf("Class 9", "Class 10", "Class 11", "Class 12"),
            boards = listOf("CBSE", "ICSE", "State Board"),
            description = "Algebra, Trigonometry, Calculus, Coordinate Geometry & Probability"
        ),
        SubjectItem(
            id = "physics",
            name = "Physics",
            iconName = "Bolt",
            colorHex = 0xFF0284C7,
            totalChapters = 12,
            classLevels = listOf("Class 9", "Class 10", "Class 11", "Class 12"),
            boards = listOf("CBSE", "ICSE", "State Board"),
            description = "Mechanics, Electromagnetism, Optics, Thermodynamics & Modern Physics"
        ),
        SubjectItem(
            id = "chemistry",
            name = "Chemistry",
            iconName = "Science",
            colorHex = 0xFF059669,
            totalChapters = 13,
            classLevels = listOf("Class 9", "Class 10", "Class 11", "Class 12"),
            boards = listOf("CBSE", "ICSE", "State Board"),
            description = "Chemical Bonding, Organic Chemistry, Periodic Trends & Stoichiometry"
        ),
        SubjectItem(
            id = "biology",
            name = "Biology",
            iconName = "Biotech",
            colorHex = 0xFF10B981,
            totalChapters = 11,
            classLevels = listOf("Class 9", "Class 10", "Class 11", "Class 12"),
            boards = listOf("CBSE", "ICSE", "State Board"),
            description = "Cellular Biology, Human Physiology, Genetics, Evolution & Ecology"
        ),
        SubjectItem(
            id = "cs_coding",
            name = "Computer Science",
            iconName = "Code",
            colorHex = 0xFF8B5CF6,
            totalChapters = 10,
            classLevels = listOf("Class 9", "Class 10", "Class 11", "Class 12"),
            boards = listOf("CBSE", "ICSE", "State Board"),
            description = "Python Programming, Data Structures, Boolean Algebra & Networking"
        )
    )

    val chapters = listOf(
        ChapterItem(
            id = "math_ch1",
            subjectId = "math",
            number = 1,
            title = "Real Numbers & Polynomials",
            summary = "Fundamental Theorem of Arithmetic, Euclidean Algorithm, zeroes of polynomials and factorization.",
            readingTimeMinutes = 15,
            topics = listOf("Fundamental Theorem of Arithmetic", "Irrational Numbers Proof", "Relationship between zeroes & coefficients"),
            keyFormulasOrNotes = listOf(
                "LCM(a, b) * HCF(a, b) = a * b",
                "For quadratic polynomial ax² + bx + c: Sum of zeroes α + β = -b/a, Product αβ = c/a",
                "Discriminant D = b² - 4ac determines nature of roots."
            ),
            content = """
                # Real Numbers & Polynomials
                
                ## 1. Fundamental Theorem of Arithmetic
                Every composite number can be expressed (factorized) as a product of primes, and this factorization is unique, apart from the order in which the prime factors occur.
                
                ## 2. Rational & Irrational Numbers
                A real number is rational if it can be written in the form p/q where p, q are integers and q ≠ 0. If it cannot be expressed in this form, it is irrational (e.g., √2, √3, π).
                
                ## 3. Quadratic Polynomials
                A polynomial of degree 2 is a quadratic polynomial. If α and β are the zeroes of P(x) = ax² + bx + c, then:
                • Sum of zeroes: α + β = -b/a
                • Product of zeroes: α * β = c/a
                
                Discriminant Analysis:
                - If D > 0: Two distinct real roots
                - If D = 0: Two equal real roots
                - If D < 0: No real roots (complex roots)
            """.trimIndent(),
            sampleQuestions = listOf(
                QuestionItem(
                    id = "q_math_1",
                    questionText = "If HCF(306, 657) = 9, what is LCM(306, 657)?",
                    options = listOf("22338", "21560", "22440", "19870"),
                    correctIndex = 0,
                    explanation = "LCM * HCF = a * b. Therefore LCM = (306 * 657) / 9 = 34 * 657 = 22338."
                ),
                QuestionItem(
                    id = "q_math_2",
                    questionText = "What is the sum of zeroes of the quadratic polynomial 2x² - 8x + 6?",
                    options = listOf("2", "4", "-4", "3"),
                    correctIndex = 1,
                    explanation = "Sum of zeroes = -b/a = -(-8)/2 = 8/2 = 4."
                )
            )
        ),
        ChapterItem(
            id = "physics_ch1",
            subjectId = "physics",
            number = 1,
            title = "Electricity & Circuits",
            summary = "Ohm's Law, electric potential, resistance in series and parallel, electric power and heating effect.",
            readingTimeMinutes = 18,
            topics = listOf("Electric Current and Circuit", "Electric Potential and Potential Difference", "Ohm's Law", "Factors affecting resistance", "Joule's Law of Heating"),
            keyFormulasOrNotes = listOf(
                "Ohm's Law: V = I * R",
                "Resistance: R = ρ * (L / A)",
                "Series Resistance: Rs = R1 + R2 + R3",
                "Parallel Resistance: 1/Rp = 1/R1 + 1/R2 + 1/R3",
                "Joule's Heating: H = I² * R * t"
            ),
            content = """
                # Electricity & Circuits
                
                ## 1. Electric Current
                Electric current is expressed by the amount of charge flowing through a particular area in unit time: I = Q / t. The SI unit of electric current is Ampere (A).
                
                ## 2. Potential Difference
                Electric potential difference (V) between two points in an electric circuit is the work done to move a unit charge from one point to the other: V = W / Q. Unit: Volt (V).
                
                ## 3. Ohm's Law
                The potential difference V across the ends of a given metallic wire in an electric circuit is directly proportional to the current I flowing through it, provided its temperature remains constant: V = I * R.
                
                ## 4. Joule's Heating Effect
                When an electric current flows through a high resistance conductor, it gets heated. Heat produced: H = I²Rt = VIt = (V²/R)t.
            """.trimIndent(),
            sampleQuestions = listOf(
                QuestionItem(
                    id = "q_phy_1",
                    questionText = "Three resistors of 2Ω, 3Ω, and 6Ω are connected in parallel. What is their equivalent resistance?",
                    options = listOf("11Ω", "1Ω", "2Ω", "0.5Ω"),
                    correctIndex = 1,
                    explanation = "1/Rp = 1/2 + 1/3 + 1/6 = (3 + 2 + 1)/6 = 6/6 = 1. Therefore Rp = 1Ω."
                ),
                QuestionItem(
                    id = "q_phy_2",
                    questionText = "Which physical quantity has the SI unit Ohm-meter (Ω·m)?",
                    options = listOf("Resistance", "Electrical Resistivity", "Electric Conductivity", "Electric Power"),
                    correctIndex = 1,
                    explanation = "Resistivity (ρ) has the SI unit Ohm-meter (Ω·m) since R = ρL/A -> ρ = R*A/L = Ω*m²/m = Ω·m."
                )
            )
        ),
        ChapterItem(
            id = "chem_ch1",
            subjectId = "chemistry",
            number = 1,
            title = "Chemical Reactions & Equations",
            summary = "Balancing chemical equations, types of chemical reactions, oxidation, reduction, and corrosion.",
            readingTimeMinutes = 16,
            topics = listOf("Balancing Chemical Equations", "Combination Reactions", "Decomposition Reactions", "Redox Reactions"),
            keyFormulasOrNotes = listOf(
                "Combination: A + B -> AB",
                "Decomposition: AB -> A + B (thermal, electrolytic, photochemical)",
                "Displacement: A + BC -> AC + B",
                "Redox: Oxidation is gain of O or loss of H / loss of electrons; Reduction is gain of H or gain of electrons."
            ),
            content = """
                # Chemical Reactions & Equations
                
                A chemical reaction occurs when one or more substances transform into new substances with different physical and chemical properties.
                
                ## Indicators of a Chemical Reaction:
                1. Change in state
                2. Change in colour
                3. Evolution of a gas
                4. Change in temperature
                5. Formation of a precipitate
                
                ## Law of Conservation of Mass:
                Mass can neither be created nor destroyed in a chemical reaction. Therefore, the total number of atoms of each element remains identical on both sides of a balanced equation.
            """.trimIndent(),
            sampleQuestions = listOf(
                QuestionItem(
                    id = "q_chem_1",
                    questionText = "What type of reaction is 2H₂ + O₂ -> 2H₂O?",
                    options = listOf("Combination Reaction", "Decomposition Reaction", "Double Displacement", "Precipitation"),
                    correctIndex = 0,
                    explanation = "Two reactants combine to form a single compound, which is the definition of a combination reaction."
                )
            )
        )
    )

    val languageCourses = listOf(
        LanguageLesson(
            id = "lang_en_1",
            language = "English",
            title = "Academic & Technical Vocabulary",
            category = "Vocabulary",
            level = "Secondary",
            items = listOf(
                VocabularyCard(
                    word = "Hypothesis",
                    phonetic = "/haɪˈpɒθəsɪs/",
                    meaning = "A proposed explanation made on the basis of limited evidence as a starting point for further investigation.",
                    exampleSentence = "The scientist conducted several trials to test her hypothesis.",
                    translation = "परिकल्पना / પ્રાકલ્પના"
                ),
                VocabularyCard(
                    word = "Equilibrium",
                    phonetic = "/ˌiːkwɪˈlɪbriəm/",
                    meaning = "A state in which opposing forces or influences are balanced.",
                    exampleSentence = "The chemical reaction reached dynamic equilibrium after 10 minutes.",
                    translation = "संतुलन / સંતુલન"
                ),
                VocabularyCard(
                    word = "Corroborate",
                    phonetic = "/kəˈrɒbəreɪt/",
                    meaning = "Confirm or give support to a statement, theory, or finding.",
                    exampleSentence = "The experiment results corroborated previous research findings.",
                    translation = "पुष्टि करना / સમર્થન આપવું"
                )
            ),
            practiceQuiz = listOf(
                QuestionItem(
                    id = "q_lang_en_1",
                    questionText = "Which word means 'a state of balance among opposing forces'?",
                    options = listOf("Hypothesis", "Equilibrium", "Trajectory", "Oscillation"),
                    correctIndex = 1,
                    explanation = "Equilibrium refers to a state of physical, chemical, or emotional balance."
                )
            )
        ),
        LanguageLesson(
            id = "lang_hi_1",
            language = "Hindi",
            title = "हिंदी व्याकरण: समास और संधि",
            category = "Grammar",
            level = "Higher Secondary",
            items = listOf(
                VocabularyCard(
                    word = "तत्पुरुष समास",
                    phonetic = "Tatpurush Samas",
                    meaning = "जिस समास में उत्तरपद प्रधान होता है तथा दोनों पदों के बीच का कारक चिह्न लुप्त हो जाता है।",
                    exampleSentence = "राजपुत्र = राजा का पुत्र (संबंध तत्पुरुष)।",
                    translation = "Dependent determinative compound"
                ),
                VocabularyCard(
                    word = "द्वंद्व समास",
                    phonetic = "Dvandva Samas",
                    meaning = "जिस समास में दोनों पद समान रूप से प्रधान हों और 'और', 'या' का लोप हो।",
                    exampleSentence = "माता-पिता = माता और पिता; दिन-रात = दिन और रात।",
                    translation = "Copulative compound"
                )
            ),
            practiceQuiz = listOf(
                QuestionItem(
                    id = "q_lang_hi_1",
                    questionText = "'नीलकंठ' में कौन-सा समास है जब इसका विग्रह 'नीला है कंठ जिसका (शिव)' हो?",
                    options = listOf("बहुव्रीहि समास", "कर्मधारय समास", "द्विगु समास", "अव्ययीभाव समास"),
                    correctIndex = 0,
                    explanation = "जब दोनों पद मिलकर किसी तीसरे पद की ओर संकेत करते हैं, तो बहुव्रीहि समास होता है।"
                )
            )
        ),
        LanguageLesson(
            id = "lang_gu_1",
            language = "Gujarati",
            title = "ગુજરાતી વ્યાકરણ: અલંકાર અને છંદ",
            category = "Grammar",
            level = "Secondary",
            items = listOf(
                VocabularyCard(
                    word = "ઉપમા અલંકાર",
                    phonetic = "Upama Alankar",
                    meaning = "જ્યારે બે જુદી જુદી વસ્તુઓની સરખામણી 'જેવો', 'જેવી', 'જેવું' જેવા શબ્દોથી કરવામાં આવે ત્યારે.",
                    exampleSentence = "દમયંતીનું મુખ ચંદ્ર જેવું સુંદર છે.",
                    translation = "Simile in Gujarati Literature"
                ),
                VocabularyCard(
                    word = "રૂપક અલંકાર",
                    phonetic = "Rupak Alankar",
                    meaning = "જ્યારે ઉપમેય અને ઉપમાન વચ્ચે અભેદતા દર્શાવવામાં આવે.",
                    exampleSentence = "બપોર એક મોટું શિકારી કૂતરું છે.",
                    translation = "Metaphor in Gujarati Literature"
                )
            ),
            practiceQuiz = listOf(
                QuestionItem(
                    id = "q_lang_gu_1",
                    questionText = "'તેનું મુખ કમળ જેવું કોમળ છે' - કયો અલંકાર છે?",
                    options = listOf("ઉપમા અલંકાર", "રૂપક અલંકાર", "શ્લેષ અલંકાર", "યમક અલંકાર"),
                    correctIndex = 0,
                    explanation = "'જેવું' શબ્દ દ્વારા સીધી સરખામણી કરવામાં આવી હોવાથી ઉપમા અલંકાર બને છે."
                )
            )
        ),
        LanguageLesson(
            id = "lang_sa_1",
            language = "Sanskrit",
            title = "संस्कृत व्याकरण: सन्धि एवं सुभाषितम्",
            category = "Grammar",
            level = "Secondary",
            items = listOf(
                VocabularyCard(
                    word = "दीर्घसन्धिः",
                    phonetic = "Deergha Sandhi",
                    meaning = "अकः सवर्णे दीर्घः — जब ह्रस्व या दीर्घ अ, इ, उ, ऋ के बाद समान स्वर आए तो दोनों मिलकर दीर्घ हो जाते हैं।",
                    exampleSentence = "विद्या + आलयः = विद्यालयः; देव + आनन्दः = देवानन्दः।",
                    translation = "Long vowel sandhi in Sanskrit"
                ),
                VocabularyCard(
                    word = "सुभाषितम्",
                    phonetic = "Subhashitam",
                    meaning = "उद्यमेन हि सिध्यन्ति कार्याणि न मनोरथैः। न हि सुप्तस्य सिंहस्य प्रविशन्ति मुखे मृगाः॥",
                    exampleSentence = "परिश्रम से ही कार्य सिद्ध होते हैं, केवल इच्छा करने से नहीं।",
                    translation = "Wise proverb / Golden saying"
                )
            ),
            practiceQuiz = listOf(
                QuestionItem(
                    id = "q_lang_sa_1",
                    questionText = "'विद्या + आलयः' इत्यस्य सन्धिरूपं किम्?",
                    options = listOf("विद्यालयः", "विद्य्यालयः", "विद्यलयः", "विद्यालयम्"),
                    correctIndex = 0,
                    explanation = "आ + आ = आ (अकः सवर्णे दीर्घः नियमेन 'विद्यालयः' भवति)।"
                )
            )
        )
    )

    val codingCourses = listOf(
        CodingCourse(
            id = "code_python",
            title = "Python for Students",
            languageName = "Python",
            iconSymbol = "🐍",
            description = "Learn variables, loops, functions, lists, and logic building.",
            lessons = listOf(
                CodingLesson(
                    id = "py_l1",
                    title = "1. Variables & Data Types",
                    conceptExplanation = "In Python, variables store data values without needing explicit type declarations. Common types are integers, floats, strings, and booleans.",
                    codeSnippet = """
                        student_name = "Aarav"
                        marks = 95
                        is_passed = True
                        percentage = 95.5
                        
                        print(f"Student: {student_name}")
                        print(f"Score: {percentage}% | Passed: {is_passed}")
                    """.trimIndent(),
                    expectedOutput = "Student: Aarav\nScore: 95.5% | Passed: True",
                    challengePrompt = "Create a variable called 'subject' with value 'Physics' and print it in uppercase.",
                    solutionCode = "subject = 'Physics'\nprint(subject.upper())"
                ),
                CodingLesson(
                    id = "py_l2",
                    title = "2. Conditionals & Logic",
                    conceptExplanation = "Use if, elif, and else to control program flow based on conditions.",
                    codeSnippet = """
                        grade = 85
                        if grade >= 90:
                            print("Outstanding: A+")
                        elif grade >= 80:
                            print("Excellent: A")
                        else:
                            print("Keep practicing!")
                    """.trimIndent(),
                    expectedOutput = "Excellent: A",
                    challengePrompt = "Write a condition checking if a number n = 14 is even or odd.",
                    solutionCode = "n = 14\nif n % 2 == 0:\n    print('Even')\nelse:\n    print('Odd')"
                ),
                CodingLesson(
                    id = "py_l3",
                    title = "3. Loops & Problem Solving",
                    conceptExplanation = "Loops repeat a block of code. For loops iterate over ranges or lists; while loops repeat while a condition is True.",
                    codeSnippet = """
                        # Sum of first 5 natural numbers
                        total = 0
                        for i in range(1, 6):
                            total += i
                        print("Sum:", total)
                    """.trimIndent(),
                    expectedOutput = "Sum: 15",
                    challengePrompt = "Print the square of each number from 1 to 4 using a loop.",
                    solutionCode = "for x in range(1, 5):\n    print(x ** 2)"
                )
            )
        ),
        CodingCourse(
            id = "code_web",
            title = "Web Development: HTML & CSS",
            languageName = "HTML/CSS",
            iconSymbol = "🌐",
            description = "Build structured web pages, headings, cards, and interactive layouts.",
            lessons = listOf(
                CodingLesson(
                    id = "web_l1",
                    title = "1. Semantic HTML Structure",
                    conceptExplanation = "HTML provides the skeleton of web pages using tags like <h1>, <p>, <section>, and <button>.",
                    codeSnippet = """
                        <!DOCTYPE html>
                        <html>
                          <body>
                            <h1>EduSphere Portal</h1>
                            <p>Secondary & Higher Secondary Learning</p>
                            <button>Start Learning</button>
                          </body>
                        </html>
                    """.trimIndent(),
                    expectedOutput = "[Web Page with Heading 'EduSphere Portal', paragraph and button]",
                    challengePrompt = "Add an unordered list (<ul>) with two list items (<li>) for 'Math' and 'Physics'.",
                    solutionCode = "<ul>\n  <li>Math</li>\n  <li>Physics</li>\n</ul>"
                )
            )
        ),
        CodingCourse(
            id = "code_java",
            title = "Java & OOP Principles",
            languageName = "Java",
            iconSymbol = "☕",
            description = "Classes, objects, methods, encapsulation, and inheritance.",
            lessons = listOf(
                CodingLesson(
                    id = "java_l1",
                    title = "1. Classes & Objects",
                    conceptExplanation = "Java is an object-oriented language. Everything is associated with classes and objects.",
                    codeSnippet = """
                        public class Student {
                            String name;
                            int grade;
                            
                            public Student(String n, int g) {
                                name = n;
                                grade = g;
                            }
                            
                            public void display() {
                                System.out.println(name + " - Grade " + grade);
                            }
                        }
                    """.trimIndent(),
                    expectedOutput = "Output depends on main() instantiation: 'Diya - Grade 10'",
                    challengePrompt = "Declare a method called 'isHighScorer' returning boolean if score > 90.",
                    solutionCode = "public boolean isHighScorer(int score) {\n    return score > 90;\n}"
                )
            )
        ),
        CodingCourse(
            id = "code_cpp",
            title = "C++ & Data Structures",
            languageName = "C++",
            iconSymbol = "⚡",
            description = "Pointers, arrays, vectors, and algorithmic problem solving.",
            lessons = listOf(
                CodingLesson(
                    id = "cpp_l1",
                    title = "1. Arrays & Vectors",
                    conceptExplanation = "Vectors in C++ are dynamic arrays that resize automatically when elements are inserted.",
                    codeSnippet = """
                        #include <iostream>
                        #include <vector>
                        using namespace std;
                        
                        int main() {
                            vector<int> marks = {85, 92, 78};
                            marks.push_back(99);
                            cout << "Total items: " << marks.size() << endl;
                            return 0;
                        }
                    """.trimIndent(),
                    expectedOutput = "Total items: 4",
                    challengePrompt = "Access the second element in the vector 'marks'.",
                    solutionCode = "cout << marks[1];"
                )
            )
        )
    )
}
