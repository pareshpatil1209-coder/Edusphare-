package com.example.data.model

object AuthorizedEducationalCatalog {

    val resources: List<EducationalResource> = listOf(
        // GSEB Class 10 - Mathematics (English Medium)
        EducationalResource(
            id = "gseb_10_math_tb_en",
            title = "Class 10 Mathematics Standard Textbook",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "Chapter 1: Real Numbers",
            resourceType = ResourceType.E_TEXTBOOK,
            language = "English",
            publisher = "Gujarat State Board of School Textbooks (GSSTB)",
            officialSourceUrl = "https://gujarat-education.gov.in/TextBook/index.htm",
            licenseNotice = "Published by Gujarat State Board of School Textbooks (GSSTB) under Open Educational Portal Guidelines. For non-commercial student study use.",
            description = "Official GSEB Class 10 Mathematics Chapter 1 study guide covering Euclid's Lemma, Fundamental Theorem of Arithmetic, and proof of irrationality.",
            fullTextContent = """# GSEB Class 10 Mathematics: Chapter 1 - Real Numbers

## 1.1 Introduction
In Class 9, you began your exploration of the world of real numbers and encountered irrational numbers. In this chapter, we continue our discussion on real numbers. We begin with two very important properties of positive integers: Euclid’s division algorithm and the Fundamental Theorem of Arithmetic.

## 1.2 The Fundamental Theorem of Arithmetic
Every composite number can be expressed (factorised) as a product of primes, and this factorisation is unique, apart from the order in which the prime factors occur.

Theorem 1.1: Given positive integers a and b, there exist unique integers q and r satisfying:
a = bq + r,  where 0 ≤ r < b.

### Key Applications:
1. Finding HCF and LCM of positive integers using prime factorisation method:
   - HCF(a, b) = Product of the smallest power of each common prime factor involved in the numbers.
   - LCM(a, b) = Product of the greatest power of each prime factor involved in the numbers.
   - Formula: HCF(a, b) × LCM(a, b) = a × b (Valid for two numbers).

## 1.3 Revisiting Irrational Numbers
Theorem 1.2: Let p be a prime number. If p divides a², then p divides a, where a is a positive integer.
Theorem 1.3: √2, √3, √5 are irrational numbers.
Proof method: Proof by Contradiction.
Assume √2 = a/b where a and b are co-prime positive integers.
Squaring both sides: 2 = a²/b² => 2b² = a².
Since 2 divides a², 2 divides a. Let a = 2c.
Then 2b² = 4c² => b² = 2c². Hence 2 divides b².
This implies 2 divides both a and b, contradicting the premise that a and b are co-prime!
Therefore, √2 is irrational.""",
            estimatedReadMinutes = 15
        ),

        // GSEB Class 10 - Mathematics (Gujarati Medium)
        EducationalResource(
            id = "gseb_10_math_tb_gu",
            title = "ધોરણ ૧૦ ગણિત પાઠ્યપુસ્તક: પ્રકરણ ૧ વાસ્તવિક સંખ્યાઓ",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "પ્રકરણ ૧: વાસ્તવિક સંખ્યાઓ",
            resourceType = ResourceType.E_TEXTBOOK,
            language = "Gujarati",
            publisher = "ગુજરાત રાજ્ય શાળા પાઠ્યપુસ્તક મંડળ (GSSTB)",
            officialSourceUrl = "https://gujarat-education.gov.in/TextBook/index.htm",
            licenseNotice = "ગુજરાત રાજ્ય શાળા પાઠ્યપુસ્તક મંડળ દ્વારા પ્રકાશિત અધિકૃત પાઠ્યક્રમ સામગ્રી. વિદ્યાર્થીઓના શૈક્ષણિક અભ્યાસ હેતુ માટે.",
            description = "જીએસઈબી ધોરણ ૧૦ ગણિતનું પ્રકરણ ૧: અંકગણિતનું મૂળભૂત પ્રમેય, ગુ.સા.અ., લ.સા.અ. અને અસંમેય સંખ્યાઓની સાબિતી.",
            fullTextContent = """# ધોરણ ૧૦ ગણિત: પ્રકરણ ૧ - વાસ્તવિક સંખ્યાઓ

## ૧.૧ પ્રસ્તાવના
આ પ્રકરણમાં આપણે ધન પૂર્ણાંકોના બે મહત્વના ગુણધર્મો: યુક્લિડની ભાગપ્રવિધિ અને અંકગણિતના મૂળભૂત પ્રમેયનો અભ્યાસ કરીશું.

## ૧.૨ અંકગણિતનું મૂળભૂત પ્રમેય
કોઈપણ વિભાજ્ય સંખ્યાને તેના અવિભાજ્ય અવયવોના ગુણાકાર તરીકે અનન્ય રીતે દર્શાવી શકાય છે.

પ્રમેય ૧.૧: યુક્લિડનું ભાગાકારનું પૂર્વપ્રમેય:
આપેલ ધન પૂર્ણાંકો a અને b ને સંગત અનન્ય અનૃણ પૂર્ણાંકો q અને r એવા મળે કે જેથી:
a = bq + r,  જ્યાં 0 ≤ r < b.

### મુખ્ય સૂત્રો અને નિયમો:
- ગુ.સા.અ. (a, b) × લ.સા.અ. (a, b) = a × b
- બે ધન પૂર્ણાંકો માટે આ સંબંધ હંમેશા સાચો છે.

## ૧.૩ અસંમેય સંખ્યાઓનું પુનરાવર્તન
પ્રમેય: સાબિત કરો કે √૫ અસંમેય સંખ્યા છે.
સાબિતી: ધારો કે √૫ સંમેય સંખ્યા છે.
√૫ = a/b (જ્યાં a અને b પરસ્પર અવિભાજ્ય પૂર્ણાંકો છે અને b ≠ 0).
બંને બાજુ વર્ગ કરતાં: ૫ = a²/b² => a² = ૫b².
આથી ૫ એ a² નો અવયવ છે, જેથી ૫ એ a નો પણ અવયવ બને.
આ પદ્ધતિ અનિષ્ટ આપત્તિ રીત (Contradiction Method) કહેવાય છે.""",
            estimatedReadMinutes = 15
        ),

        // GSEB Class 10 - Science & Tech (English Medium)
        EducationalResource(
            id = "gseb_10_sci_ch1",
            title = "Class 10 Science: Chemical Reactions & Equations",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Science & Tech",
            chapter = "Chapter 1: Chemical Reactions & Equations",
            resourceType = ResourceType.STUDY_NOTE,
            language = "English",
            publisher = "NCERT & DIKSHA National Educational Portal",
            officialSourceUrl = "https://diksha.gov.in/explore",
            licenseNotice = "DIKSHA Open Access Learning Resources. Hosted for academic revision purposes.",
            description = "Concise notes covering combination, decomposition, displacement, double displacement, redox reactions, corrosion, and rancidity.",
            fullTextContent = """# Science & Technology: Chemical Reactions & Equations

## 1. What is a Chemical Reaction?
A chemical reaction involves chemical change where one or more reactants transform into products with completely new chemical properties.
Indicators of a reaction:
- Change in state
- Change in colour
- Evolution of a gas
- Change in temperature
- Formation of a precipitate

## 2. Balanced Chemical Equations
According to the Law of Conservation of Mass (Lavoisier), mass can neither be created nor destroyed in a chemical reaction. Therefore, the total number of atoms of each element on the reactant side must equal that on the product side.
Example:
3Fe(s) + 4H2O(g) -> Fe3O4(s) + 4H2(g)

## 3. Types of Chemical Reactions:
1. Combination Reaction: Two or more reactants combine to form a single product.
   CaO(s) + H2O(l) -> Ca(OH)2(aq) + Heat (Slaked lime formation).
2. Decomposition Reaction: A single reactant breaks down into simpler products when heated or subjected to electricity/light.
   2FeSO4(s) --Heat-> Fe2O3(s) + SO2(g) + SO3(g).
3. Displacement Reaction: More reactive element displaces less reactive element.
   Fe(s) + CuSO4(aq) -> FeSO4(aq) + Cu(s) (Blue to green).
4. Double Displacement Reaction: Exchange of ions between reactants.
   Na2SO4(aq) + BaCl2(aq) -> BaSO4(s) (white ppt) + 2NaCl(aq).
5. Oxidation & Reduction (Redox):
   - Oxidation: Gain of oxygen or loss of hydrogen/electrons.
   - Reduction: Loss of oxygen or gain of hydrogen/electrons.""",
            estimatedReadMinutes = 12
        ),

        // GSEB Class 10 - Official Board Blueprint
        EducationalResource(
            id = "gseb_10_blueprint_math",
            title = "GSEB Class 10 Mathematics Annual Examination Blueprint",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "All Chapters",
            resourceType = ResourceType.BLUEPRINT,
            language = "English",
            publisher = "Gujarat Secondary & Higher Secondary Education Board (Gandhinagar)",
            officialSourceUrl = "https://gseb.org",
            licenseNotice = "Official public notification and examination blueprint released by GSEB Examination Committee.",
            description = "Detailed weightage of marks per chapter, question format division (Section A, B, C, D), and model paper design.",
            fullTextContent = """# GSEB Class 10 Mathematics Examination Blueprint

## Paper Structure:
- Total Marks: 80 Marks
- Time Allowed: 3 Hours (180 Minutes)
- Objective / Knowledge: 20%
- Understanding: 30%
- Application: 35%
- High Order Thinking (HOTs): 15%

## Section-wise Question Format:
- Section A: Questions 1 to 16 (1 Mark each) = 16 Marks
  - True/False (4Q)
  - Fill in the Blanks (4Q)
  - Multiple Choice Questions (4Q)
  - One word/formula answer (4Q)
- Section B: Questions 17 to 26 (2 Marks each, attempt any 9) = 18 Marks
  - Short numerical calculations, HCF/LCM, finding zeroes of polynomial.
- Section C: Questions 27 to 34 (3 Marks each, attempt any 6) = 18 Marks
  - Proving irrationality, quadratic equation problems, arithmetic progression sums.
- Section D: Questions 35 to 40 (4 Marks each, attempt any 4) = 16 Marks
  - Basic Proportionality Theorem (Thales Theorem), Pythagoras Theorem or its converse.
  - Surface area and volume word problems, Statistics (Median/Mode calculation).""",
            estimatedReadMinutes = 8
        ),

        // CBSE Class 10 - Mathematics NCERT
        EducationalResource(
            id = "cbse_10_math_ncert",
            title = "CBSE Class 10 Mathematics NCERT Textbook",
            board = "CBSE",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "Chapter 8: Introduction to Trigonometry",
            resourceType = ResourceType.E_TEXTBOOK,
            language = "English",
            publisher = "National Council of Educational Research and Training (NCERT)",
            officialSourceUrl = "https://ncert.nic.in/textbook.php",
            licenseNotice = "Published by NCERT. Licensed for national open student access.",
            description = "Trigonometric ratios of acute angles, standard angles (0°, 30°, 45°, 60°, 90°), and trigonometric identities.",
            fullTextContent = """# NCERT Class 10 Mathematics: Chapter 8 - Trigonometry

## 8.1 Introduction
The word 'trigonometry' is derived from Greek words 'tri' (three), 'gon' (sides), and 'metron' (measure). It is the study of relationships between sides and angles of a triangle.

## 8.2 Trigonometric Ratios
Consider right-angled triangle ABC right angled at B:
- sin A = Opposite side / Hypotenuse = BC / AC
- cos A = Adjacent side / Hypotenuse = AB / AC
- tan A = Opposite side / Adjacent side = BC / AB
- cosec A = 1 / sin A = AC / BC
- sec A = 1 / cos A = AC / AB
- cot A = 1 / tan A = AB / BC

## 8.3 Trigonometric Identities:
1. sin² A + cos² A = 1
2. 1 + tan² A = sec² A (for 0° ≤ A < 90°)
3. 1 + cot² A = cosec² A (for 0° < A ≤ 90°)""",
            estimatedReadMinutes = 14
        ),

        // GSEB Class 12 Science - Physics (Electric Charges)
        EducationalResource(
            id = "gseb_12_phys_ch1",
            title = "Class 12 Physics: Electric Charges and Fields",
            board = "GSEB",
            classLevel = "Class 12 Science",
            subject = "Physics",
            chapter = "Chapter 1: Electric Charges and Fields",
            resourceType = ResourceType.STUDY_NOTE,
            language = "English",
            publisher = "GSSTB & NCERT",
            officialSourceUrl = "https://gujarat-education.gov.in/TextBook/index.htm",
            licenseNotice = "Authorized GSEB & NCERT Higher Secondary Science Syllabus Resource.",
            description = "Detailed Higher Secondary notes on Coulomb's law, electric dipole, flux, and Gauss's law applications.",
            fullTextContent = """# Higher Secondary Class 12: Electric Charges and Fields

## 1. Electric Charge Properties:
1. Additivity: Total charge is algebraic sum: Q = q1 + q2 + ... + qn.
2. Conservation: Total charge of an isolated system remains constant.
3. Quantization: Q = ±ne, where e = 1.602 × 10⁻¹⁹ C.

## 2. Coulomb's Law:
The electrostatic force between two point charges q1 and q2 separated by distance r in vacuum:
F = (1 / 4πε0) × (|q1 × q2| / r²)
where ε0 = 8.854 × 10⁻¹² C² N⁻¹ m⁻² (permittivity of free space).
1 / (4πε0) ≈ 9 × 10⁹ N m² C⁻².

## 3. Gauss's Law:
The total electric flux through any closed surface S is equal to 1/ε0 times the total charge enclosed:
Φ = ∮ E · dA = Q_enclosed / ε0.""",
            estimatedReadMinutes = 16
        )
    )

    val sampleFlashcardDecks: List<FlashcardDeck> = listOf(
        FlashcardDeck(
            id = "deck_math_10_formulas",
            title = "Class 10 Math: Theorems & Formulas",
            subject = "Mathematics",
            board = "GSEB",
            classLevel = "Class 10",
            cards = listOf(
                FlashcardItem(
                    id = "c1",
                    front = "What is the Fundamental Theorem of Arithmetic?",
                    back = "Every composite number can be uniquely factored as a product of prime numbers, regardless of the order of factors.",
                    hint = "Related to prime factorisation uniqueness",
                    keyConcept = "Real Numbers"
                ),
                FlashcardItem(
                    id = "c2",
                    front = "State the Quadratic Formula for roots of ax² + bx + c = 0.",
                    back = "x = (-b ± √(b² - 4ac)) / (2a)",
                    hint = "Look at the discriminant D = b² - 4ac",
                    keyConcept = "Quadratic Equations"
                ),
                FlashcardItem(
                    id = "c3",
                    front = "State Thales Theorem (Basic Proportionality Theorem).",
                    back = "If a line is drawn parallel to one side of a triangle intersecting the other two sides, then it divides the two sides in the same ratio.",
                    hint = "AD/DB = AE/EC",
                    keyConcept = "Triangles"
                ),
                FlashcardItem(
                    id = "c4",
                    front = "Write the three fundamental Trigonometric Identities.",
                    back = "1. sin² θ + cos² θ = 1\n2. 1 + tan² θ = sec² θ\n3. 1 + cot² θ = cosec² θ",
                    hint = "Pythagorean trigonometric relations",
                    keyConcept = "Trigonometry"
                )
            )
        ),
        FlashcardDeck(
            id = "deck_guj_vyakaran",
            title = "Gujarati Vyakarana: Sandhi & Samas",
            subject = "Gujarati",
            board = "GSEB",
            classLevel = "Class 10",
            cards = listOf(
                FlashcardItem(
                    id = "g_c1",
                    front = "સૂર્યોદય શબ્દની સંધિ છૂટી પાડો.",
                    back = "સૂર્ય + ઉદય = સૂર્યોદય (ગુણ સંધિ)",
                    hint = "અ/આ + ઉ = ઓ",
                    keyConcept = "સંધિ"
                ),
                FlashcardItem(
                    id = "g_c2",
                    front = "દ્વન્દ્વ સમાસ એટલે શું? ઉદાહરણ આપો.",
                    back = "જે સમાસમાં બંને પદો સ્વતંત્ર અને સમાન મહત્વ ધરાવતા હોય, જેમ કે: ભાઈ-બહેન, માતા-પિતા, સુખ-દુઃખ.",
                    hint = "'અને' કે 'કે' થી છૂટા પડે",
                    keyConcept = "સમાસ"
                )
            )
        )
    )
}
