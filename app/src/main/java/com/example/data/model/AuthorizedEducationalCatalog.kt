package com.example.data.model

object AuthorizedEducationalCatalog {

    val resources: List<EducationalResource> = listOf(
        // GSEB Class 10 - Mathematics
        EducationalResource(
            id = "gseb_10_math_tb",
            title = "GSEB Class 10 Mathematics (Ganit)",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "Full Textbook & Important Theorems",
            language = "English",
            resourceType = "E-Textbook",
            source = "GSEB - Gujarat State Board of School Textbooks (GSSTB)",
            officialUrl = "https://gseb.org",
            licensing = "Government of Gujarat Open Educational Resource for Free Student Use",
            editionYear = "2024-2025",
            summary = "Official GSEB Class 10 Mathematics syllabus covering Real Numbers, Polynomials, Linear Equations, Triangles (Basic Proportionality Theorem), Trigonometry, and Statistics.",
            fullContent = """
                # GSEB Class 10 Mathematics — Core Syllabus & Formula Reference
                
                ## Board Blueprint Summary:
                • Section A (16 Marks): Objective, MCQs, True/False, Fill in blanks
                • Section B (20 Marks): 10 questions of 2 marks each (Calculations)
                • Section C (24 Marks): 8 questions of 3 marks each (Proofs and Analytical)
                • Section D (20 Marks): 5 questions of 4 marks each (Theorems and Constructions)
                
                ---
                ### Chapter 1: Real Numbers
                • Fundamental Theorem of Arithmetic: Every composite number can be expressed as a product of prime numbers uniquely.
                • Formula: HCF(a, b) × LCM(a, b) = a × b
                • Proof of irrationality of √2, √3, √5 using method of contradiction.
                
                ### Chapter 6: Triangles (Crucial 4-Mark Theorem)
                • Theorem 6.1 (Basic Proportionality Theorem / Thales Theorem):
                  If a line is drawn parallel to one side of a triangle intersecting the other two sides, then it divides the two sides in the same ratio.
                  Given: In △ABC, DE ∥ BC.
                  To prove: AD/DB = AE/EC.
                  Construction: Join BE, CD and draw DM ⊥ AC, EN ⊥ AB.
                  Proof: Area of △ADE = 1/2 × AD × EN. Area of △BDE = 1/2 × DB × EN.
                  Therefore Area(△ADE)/Area(△BDE) = AD/DB.
                  Similarly, Area(△ADE)/Area(△CDE) = AE/EC.
                  Since △BDE and △CDE are on the same base DE and between same parallels DE and BC, Area(△BDE) = Area(△CDE).
                  Hence, AD/DB = AE/EC (Proved).
                  
                ### Chapter 8: Introduction to Trigonometry
                • sin²θ + cos²θ = 1
                • 1 + tan²θ = sec²θ
                • 1 + cot²θ = cosec²θ
                • Standard values: sin 30° = 1/2, sin 45° = 1/√2, sin 60° = √3/2, cos 30° = √3/2, tan 45° = 1.
                
                ### Chapter 14: Statistics
                • Mean (Direct Method): x̄ = Σ(fᵢxᵢ) / Σfᵢ
                • Mode = l + [(f₁ - f₀) / (2f₁ - f₀ - f₂)] × h
                • Median = l + [(N/2 - cf) / f] × h
                • Empirical relationship: 3 Median = Mode + 2 Mean
            """.trimIndent()
        ),

        // GSEB Class 10 - Science
        EducationalResource(
            id = "gseb_10_science_notes",
            title = "GSEB Class 10 Science & Technology Comprehensive Study Notes",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Science",
            chapter = "Physics, Chemistry & Biology Modules",
            language = "English",
            resourceType = "Study Notes",
            source = "Gujarat State Board of School Textbooks (GSSTB) & DIKSHA",
            officialUrl = "https://diksha.gov.in",
            licensing = "Government Open Access Educational Resource",
            editionYear = "2024-2025",
            summary = "High-yield revision notes for GSEB Class 10 board exams: Chemical Reactions, Electricity, Life Processes, and Light Refraction.",
            fullContent = """
                # GSEB Class 10 Science Comprehensive Exam Notes
                
                ## Chemistry Module:
                ### Chemical Reactions & Equations
                1. Combination Reaction: CaO (Quicklime) + H₂O → Ca(OH)₂ (Slaked lime) + Heat (Exothermic)
                2. Decomposition Reaction: 2FeSO₄ (s) --[Heat]--> Fe₂O₃ (s) + SO₂ (g) + SO₃ (g)
                3. Displacement Reaction: Fe + CuSO₄ (aq, blue) → FeSO₄ (aq, green) + Cu
                4. Double Displacement (Precipitation): Na₂SO₄ + BaCl₂ → BaSO₄ ↓ (white ppt) + 2NaCl
                5. Corrosion of Metals: 4Fe + 3O₂ + 2xH₂O → 2Fe₂O₃·xH₂O (Rust)
                
                ---
                ## Physics Module:
                ### Electricity & Ohm's Law
                • Electric Current I = Q / t (Ampere)
                • Electric Potential V = W / Q (Volt)
                • Ohm's Law: V = I × R (at constant temperature)
                • Factors affecting Resistance: R = ρ × (L / A), where ρ is resistivity in Ω·m.
                • Resistors in Series: Rₛ = R₁ + R₂ + R₃
                • Resistors in Parallel: 1/Rₚ = 1/R₁ + 1/R₂ + 1/R₃
                • Joule's Law of Heating: H = I²Rt
                • Electric Power: P = VI = I²R = V²/R
                
                ---
                ## Biology Module:
                ### Life Processes
                1. Photosynthesis: 6CO₂ + 12H₂O --[Chlorophyll & Sunlight]--> C₆H₁₂O₆ + 6O₂ + 6H₂O
                2. Human Digestive System: Salivary amylase converts starch to maltose; Pepsin in stomach digests proteins; Bile in liver emulsifies fats; Trypsin and Lipase from pancreas complete digestion.
                3. Human Heart Circulation: Double circulation ensures separation of oxygenated and deoxygenated blood for high metabolic efficiency in mammals.
                4. Excretion: Nephron is structural and functional unit consisting of Bowman's capsule and renal tubule.
            """.trimIndent()
        ),

        // GSEB Class 10 - Gujarati First Language
        EducationalResource(
            id = "gseb_10_gujarati_sahitya",
            title = "GSEB Class 10 Gujarati Pratham Bhasha (સાહિત્ય અને વ્યાકરણ)",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Gujarati",
            chapter = "વ્યાકરણ, રૂઢિપ્રયોગો અને કાવ્ય પંક્તિઓ",
            language = "Gujarati",
            resourceType = "Study Notes",
            source = "GSSTB Gandhinagar - GSEB Official Publication",
            officialUrl = "https://gseb.org",
            licensing = "Gujarat Secondary Board Official School Curriculum",
            editionYear = "2024-2025",
            summary = "ગુજરાતી ધોરણ ૧૦ બોર્ડ પરીક્ષા માટે મહત્વપૂર્ણ જોડણી નિયમો, સંધિ, સમાસ, અલંકાર, રૂઢિપ્રયોગો અને સાહિત્ય સારાંશ.",
            fullContent = """
                # ધોરણ ૧૦ ગુજરાતી (પ્રથમ ભાષા) - વ્યાકરણ અને સાહિત્ય માર્ગદર્શિકા
                
                ## ૧. સમાસ પરિચય:
                • દ્વન્દ્વ સમાસ: જેમાં બંને પદો સમાન મહત્વ ધરાવતા હોય અને 'અને' કે 'કે' થી છૂટા પડે.
                  ઉદાહરણ: રાત-દિવસ (રાત અને દિવસ), હાર-જીત (હાર કે જીત).
                • તત્પુરુષ સમાસ: ઉત્તર પદ પ્રધાન હોય અને બંને પદો વિભક્તિ સંબંધથી જોડાયેલા હોય.
                  ઉદાહરણ: દેશભક્તિ (દેશ માટે ભક્તિ), ગર્ભશ્રીમંત (ગર્ભથી શ્રીમંત).
                • મધ્યમપદલોપી સમાસ: બે પદો વચ્ચે રહેલા સંબંધ દર્શાવતા પદોનો લોપ થયો હોય.
                  ઉદાહરણ: ઘોડાગાડી (ઘોડા વડે ચાલતી ગાડી), આગગાડી (આગ વડે ચાલતી ગાડી).
                • ઉપપદ સમાસ: અંતિમ પદ ક્રિયાવાચક ધાતુ દર્શાવતું હોય (નાર પ્રત્યય).
                  ઉદાહરણ: પંકજ (કાદવમાં જન્મેલું), શાંતિકર (શાંતિ કરનાર).
                  
                ---
                ## ૨. અલંકાર:
                • ઉપમા અલંકાર: ઉપમેયની સરખામણી ઉપમાન સાથે 'જેવો, જેવી, જેવું, સમાન' શબ્દોથી થાય.
                  ઉદાહરણ: દમયંતીનું મુખ ચંદ્ર જેવું સુંદર છે.
                • રૂપક અલંકાર: ઉપમેય અને ઉપમાનને એકરૂપ દર્શાવવામાં આવે.
                  ઉદાહરણ: બપોર એક મોટું શિકારી કૂતરું છે.
                • ઉત્પ્રેક્ષા અલંકાર: ઉપમેય જાણે ઉપમાન હોય તેવી સંભાવના દર્શાવાય ('જાણે, રખે, શકે').
                  ઉદાહરણ: હૈયું જાણે હિમાલય!
                  
                ---
                ## ૩. બોર્ડ માટે મહત્વના રૂઢિપ્રયોગો:
                • આંખ ઉઘડવી - સાચી પરિસ્થિતિ સમજાઈ જવી.
                • આકાશ-પાતાળ એક કરવા - ખૂબ જ સખત પરિશ્રમ કરવો.
                • સોનાનો સૂરજ ઊગવો - ખૂબ સારો અને ભાગ્યશાળી દિવસ આવવો.
                • હૈયે વળગવું - ખૂબ સ્નેહ હોવો, યાદ રહી જવું.
            """.trimIndent()
        ),

        // GSEB Class 10 - Social Science
        EducationalResource(
            id = "gseb_10_socsci_paper",
            title = "GSEB Class 10 Social Science Board Question Paper & Blueprint",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Social Science",
            chapter = "Cultural Heritage, Geography & Indian Economy",
            language = "English",
            resourceType = "Previous Year Question Paper",
            source = "Gujarat Secondary and Higher Secondary Education Board Official Examination Archive",
            officialUrl = "https://gseb.org",
            licensing = "Public Board Exam Paper Archive for Academic Practice",
            editionYear = "2024",
            summary = "GSEB Class 10 Social Science Board Examination question paper with verified 80-mark distribution, answer keys, and map work points.",
            fullContent = """
                # GSEB Class 10 Social Science Board Examination Pattern
                
                Time: 3 Hours | Total Marks: 80
                
                ### General Instructions:
                1. This question paper contains 54 questions divided into 4 Sections: A, B, C, and D.
                2. Section A contains questions 1 to 16 of 1 mark each (Objective).
                3. Section B contains questions 17 to 26 of 2 marks each.
                4. Section C contains questions 27 to 34 of 3 marks each.
                5. Section D contains questions 35 to 40 of 4 marks each (including mandatory Map work).
                
                ---
                ### Key Exam Topics:
                • Heritage of India: Cultural and Natural Heritage differentiation.
                • Sculptures & Architecture: Lothal dockyard, Sun Temple of Modhera, Stepwell of Patan (Rani ki Vav).
                • Water Resources: Multipurpose river valley projects (Sardar Sarovar Project on Narmada, Bhakra Nangal on Satluj).
                • Economic Liberalization, Privatization, and Globalization (LPG 1991 reforms).
                • Consumer Rights (COPRA 1986): Right to Safety, Right to Information, Right to Choice, Redressal Forum.
                • Mandatory Map Pointing: Kaziranga National Park (Assam), Gir National Park (Gujarat), Black Soil Region, Jute producing belt (West Bengal).
            """.trimIndent()
        ),

        // GSEB Class 12 Science - Physics
        EducationalResource(
            id = "gseb_12_physics_textbook",
            title = "GSEB Class 12 Physics (Bhautik Vigyan) Higher Secondary Notes",
            board = "GSEB",
            classLevel = "Class 12 Science",
            subject = "Physics",
            chapter = "Electrostatics, Current Electricity & Ray Optics",
            language = "English",
            resourceType = "E-Textbook",
            source = "GSSTB & Gujarat Higher Secondary Education Board",
            officialUrl = "https://gseb.org",
            licensing = "Official Gujarat Higher Secondary Textbook for Student Reference",
            editionYear = "2024-2025",
            summary = "Higher Secondary Class 12 Physics comprehensive textbook summary: Coulomb's Law, Gauss's Theorem, Kirchhoff's Laws, Optics, and Modern Physics.",
            fullContent = """
                # GSEB Class 12 Science — Physics Key Formula & Concept Sheet
                
                ## 1. Electrostatics:
                • Coulomb's Law: F = (1 / 4πε₀) × (|q₁q₂| / r²), where 1/4πε₀ = 8.99 × 10⁹ N·m²/C²
                • Gauss's Law: Total electric flux through a closed surface Φ = ∮ E · dA = q_enclosed / ε₀
                • Field due to infinitely long charged wire: E = λ / (2πε₀r)
                • Field due to infinite plane sheet of charge: E = σ / (2ε₀)
                • Capacitance of parallel plate capacitor: C = ε₀A / d (with dielectric C = kε₀A / d)
                • Energy stored in capacitor: U = 1/2 CV² = Q² / (2C)
                
                ---
                ## 2. Current Electricity:
                • Drift Velocity: v_d = -(eEτ / m), current I = n·e·A·v_d
                • Kirchhoff's Current Law (KCL): Algebraic sum of currents at a node ΣI = 0 (Conservation of Charge)
                • Kirchhoff's Voltage Law (KVL): In any closed loop ΣΔV = 0 (Conservation of Energy)
                • Wheatstone Bridge condition for null deflection: R₁/R₂ = R₃/R₄
                • Potentiometer principle: Fall of potential across uniform wire is proportional to length (V ∝ l)
                
                ---
                ## 3. Ray Optics & Optical Instruments:
                • Mirror Formula: 1/f = 1/v + 1/u, Magnification m = -v/u = h'/h
                • Snell's Law: n₁ sin i = n₂ sin r
                • Critical angle and Total Internal Reflection (TIR): sin C = 1 / n
                • Lens Maker's Formula: 1/f = (n - 1) × (1/R₁ - 1/R₂)
                • Compound Microscope Magnifying Power: M = -(L / f_o) × (D / f_e)
            """.trimIndent()
        ),

        // GSEB Class 12 Science - Chemistry
        EducationalResource(
            id = "gseb_12_chem_summary",
            title = "GSEB Class 12 Chemistry Organic & Electrochemistry Guide",
            board = "GSEB",
            classLevel = "Class 12 Science",
            subject = "Chemistry",
            chapter = "Solutions, Electrochemistry & Organic Reaction Mechanisms",
            language = "English",
            resourceType = "Study Notes",
            source = "GSEB Higher Secondary School Education Portal",
            officialUrl = "https://gseb.org",
            licensing = "Government Educational Resource for Higher Secondary Students",
            editionYear = "2024-2025",
            summary = "Essential named reactions in organic chemistry, Nernst equation derivations, Raoult's law, and coordination compounds for GSEB 12th Board.",
            fullContent = """
                # GSEB Class 12 Chemistry Quick Review
                
                ## Physical Chemistry:
                ### Electrochemistry:
                • Nernst Equation: E_cell = E°_cell - (0.0591 / n) × log₁₀([Anode] / [Cathode]) at 298 K
                • Relationship between Gibbs Energy and EMF: ΔG° = -n F E°_cell
                • Kohlrausch's Law of Independent Migration of Ions:
                  Λ°_m = ν₊ λ°₊ + ν₋ λ°₋
                
                ---
                ## Organic Chemistry — Important Named Reactions:
                1. Sandmeyer Reaction: Benzene diazonium chloride + Cu₂Cl₂/HCl → Chlorobenzene + N₂
                2. Aldol Condensation: 2CH₃CHO --[dil. NaOH]--> CH₃-CH(OH)-CH₂-CHO --[Heat]--> CH₃-CH=CH-CHO (Crotonaldehyde)
                3. Cannizzaro Reaction (aldehydes with no α-hydrogen): 2HCHO + conc. KOH → CH₃OH + HCOOK
                4. Williamson Ether Synthesis: R-X + R'-ONa → R-O-R' + NaX (Follows SN2 mechanism)
                5. Kolbe's Reaction: Phenol + NaOH + CO₂ --[400K, 4-7 atm]--> Salicylic acid
            """.trimIndent()
        ),

        // CBSE Class 10 - NCERT Mathematics
        EducationalResource(
            id = "cbse_10_ncert_math",
            title = "NCERT Class 10 Mathematics Textbook & Exemplar Solutions",
            board = "CBSE",
            classLevel = "Class 10",
            subject = "Mathematics",
            chapter = "Quadratic Equations, Arithmetic Progressions & Coordinate Geometry",
            language = "English",
            resourceType = "E-Textbook",
            source = "NCERT Official Portal (National Council of Educational Research and Training)",
            officialUrl = "https://ncert.nic.in/textbook.php",
            licensing = "NCERT Copyright Free Access for Educational and Research Purposes",
            editionYear = "2024-2025",
            summary = "Official NCERT Mathematics textbook modules with step-by-step solved examples and board-level exercises.",
            fullContent = """
                # NCERT Class 10 Mathematics — Key Concepts
                
                ## Quadratic Equations (ax² + bx + c = 0):
                • Quadratic Formula: x = [-b ± √(b² - 4ac)] / (2a)
                • Discriminant D = b² - 4ac:
                  - If D > 0: Two distinct real roots
                  - If D = 0: Two equal real roots (x = -b / 2a)
                  - If D < 0: No real roots (roots are complex conjugate)
                  
                ## Arithmetic Progressions (AP):
                • nth term: aₙ = a + (n - 1)d
                • Sum of first n terms: Sₙ = (n / 2) × [2a + (n - 1)d] = (n / 2) × [a + l]
                
                ## Coordinate Geometry:
                • Distance Formula: d = √[(x₂ - x₁)² + (y₂ - y₁)²]
                • Section Formula (internal division m₁:m₂):
                  x = (m₁x₂ + m₂x₁) / (m₁ + m₂), y = (m₁y₂ + m₂y₁) / (m₁ + m₂)
                • Midpoint Formula: ((x₁ + x₂)/2, (y₁ + y₂)/2)
            """.trimIndent()
        ),

        // CBSE Class 10 - Science NCERT
        EducationalResource(
            id = "cbse_10_ncert_science",
            title = "NCERT Class 10 Science Practice Workbook & Model Answers",
            board = "CBSE",
            classLevel = "Class 10",
            subject = "Science",
            chapter = "Light - Reflection and Refraction & Heredity",
            language = "English",
            resourceType = "Workbook",
            source = "NCERT & DIKSHA National Teachers Portal",
            officialUrl = "https://ncert.nic.in/textbook.php",
            licensing = "National Curriculum Educational Fair-Use",
            editionYear = "2024-2025",
            summary = "Ray diagram guides, lens formula numericals, sign conventions, and genetics problems.",
            fullContent = """
                # NCERT Class 10 Science Workbook Notes
                
                ## Light: Reflection and Refraction
                • New Cartesian Sign Convention:
                  - Object is always placed to the left of the mirror/lens (u is always negative).
                  - Focal length of concave mirror/lens is negative.
                  - Focal length of convex mirror/lens is positive.
                • Mirror Formula: 1/v + 1/u = 1/f; Magnification m = -v/u = h'/h
                • Lens Formula: 1/v - 1/u = 1/f; Magnification m = +v/u = h'/h
                • Power of Lens: P = 1 / f (in meters), unit is Diopter (D).
                
                ## Heredity and Evolution:
                • Gregor Johann Mendel — Father of Genetics.
                • Monohybrid Cross (Tall × Short pea plant):
                  - F1 Generation: All Tall (Tt)
                  - F2 Generation: Phenotypic ratio = 3:1 (Tall:Short); Genotypic ratio = 1:2:1 (TT:Tt:tt).
                • Dihybrid Cross (Round Yellow × Wrinkled Green):
                  - F2 Phenotypic ratio = 9:3:3:1.
                • Sex Determination in Humans:
                  - Female has XX chromosomes; Male has XY chromosomes.
                  - The father's sperm carrying X or Y chromosome determines the child's genetic sex.
            """.trimIndent()
        )
    )

    val flashcardDecks: List<FlashcardDeck> = listOf(
        FlashcardDeck(
            id = "deck_gseb_10_math",
            title = "GSEB Class 10 Math: Theorems & Formulas",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Mathematics",
            description = "High-frequency formulas and theorem statements guaranteed in GSEB board exams.",
            cards = listOf(
                FlashcardItem(
                    id = "fc_m1",
                    frontQuestion = "What is the Basic Proportionality Theorem (Thales Theorem)?",
                    backAnswer = "If a line is drawn parallel to one side of a triangle intersecting the other two sides, it divides the two sides in the same ratio (AD/DB = AE/EC).",
                    category = "Theorem",
                    hint = "Applies to a triangle with parallel line segment."
                ),
                FlashcardItem(
                    id = "fc_m2",
                    frontQuestion = "State the formula for finding the Mode of grouped data.",
                    backAnswer = "Mode = l + [(f₁ - f₀) / (2f₁ - f₀ - f₂)] × h, where l is lower boundary of modal class, f₁ is modal class frequency.",
                    category = "Statistics",
                    formulaOrRule = "Mode = l + [(f₁ - f₀)/(2f₁ - f₀ - f₂)] × h"
                ),
                FlashcardItem(
                    id = "fc_m3",
                    frontQuestion = "What is the discriminant formula and what does D = 0 signify?",
                    backAnswer = "D = b² - 4ac. When D = 0, the quadratic equation has two real and equal roots: x = -b / (2a).",
                    category = "Algebra",
                    formulaOrRule = "D = b² - 4ac; roots x = -b/(2a)"
                ),
                FlashcardItem(
                    id = "fc_m4",
                    frontQuestion = "Express the Pythagorean trigonometric identities.",
                    backAnswer = "1. sin²θ + cos²θ = 1\n2. 1 + tan²θ = sec²θ\n3. 1 + cot²θ = cosec²θ",
                    category = "Trigonometry"
                ),
                FlashcardItem(
                    id = "fc_m5",
                    frontQuestion = "State the Section Formula for internal division in ratio m₁:m₂.",
                    backAnswer = "Coordinates (x, y) = ((m₁x₂ + m₂x₁)/(m₁ + m₂), (m₁y₂ + m₂y₁)/(m₁ + m₂))",
                    category = "Coordinate Geometry"
                )
            )
        ),
        FlashcardDeck(
            id = "deck_gseb_10_science",
            title = "GSEB Class 10 Science: Reactions & Laws",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Science",
            description = "Crucial chemical equations, Ohm's law, and human physiology facts.",
            cards = listOf(
                FlashcardItem(
                    id = "fc_s1",
                    frontQuestion = "What is the chemical reaction when quicklime reacts with water?",
                    backAnswer = "CaO (s) + H₂O (l) → Ca(OH)₂ (aq) + Heat. It is an exothermic combination reaction forming slaked lime.",
                    category = "Chemistry",
                    hint = "Produces slaked lime with vigorous heat release."
                ),
                FlashcardItem(
                    id = "fc_s2",
                    frontQuestion = "State Ohm's Law and its mathematical equation.",
                    backAnswer = "The electric current passing through a conductor is directly proportional to the potential difference across its ends, provided temperature remains constant: V = I × R.",
                    category = "Physics",
                    formulaOrRule = "V = I × R"
                ),
                FlashcardItem(
                    id = "fc_s3",
                    frontQuestion = "What is the structural and functional unit of the human kidney?",
                    backAnswer = "The Nephron. It consists of the glomerulus, Bowman's capsule, and tubular structures for selective reabsorption and urine formation.",
                    category = "Biology"
                ),
                FlashcardItem(
                    id = "fc_s4",
                    frontQuestion = "State Joule's Law of Heating.",
                    backAnswer = "The heat produced in a resistor is directly proportional to the square of current, resistance, and time: H = I²Rt.",
                    category = "Physics",
                    formulaOrRule = "H = I²Rt"
                ),
                FlashcardItem(
                    id = "fc_s5",
                    frontQuestion = "What is the pH of human blood and acidic rain?",
                    backAnswer = "Human blood pH is strictly maintained between 7.35 and 7.45. Acid rain has a pH value less than 5.6.",
                    category = "Chemistry"
                )
            )
        ),
        FlashcardDeck(
            id = "deck_gujarati_grammar",
            title = "ગુજરાતી વ્યાકરણ (સમાસ, સંધિ અને અલંકાર)",
            board = "GSEB",
            classLevel = "Class 10",
            subject = "Gujarati",
            description = "બોર્ડ પરીક્ષામાં પૂછાતા મહત્વપૂર્ણ સમાસ, અલંકાર અને સંધિ નિયમો.",
            cards = listOf(
                FlashcardItem(
                    id = "fc_g1",
                    frontQuestion = "દ્વન્દ્વ સમાસ કોને કહેવાય? ઉદાહરણ આપો.",
                    backAnswer = "જે સમાસના બંને પદો વ્યાકરણની દૃષ્ટિએ સમાન મોભાના હોય અને 'અને' અથવા 'કે' સંયોજકથી જોડાયેલા હોય. ઉદાહરણ: રાત-દિવસ (રાત અને દિવસ).",
                    category = "સમાસ"
                ),
                FlashcardItem(
                    id = "fc_g2",
                    frontQuestion = "ઉત્પ્રેક્ષા અલંકારની ઓળખ કઈ રીતે થાય?",
                    backAnswer = "જ્યારે ઉપમેય જાણે ઉપમાન હોય તેવી સંભાવના દર્શાવવામાં આવે. ઓળખ શબ્દો: જાણે, રખે, શકે. ઉદાહરણ: હૈયું જાણે હિમાલય!",
                    category = "અલંકાર"
                ),
                FlashcardItem(
                    id = "fc_g3",
                    frontQuestion = "'મધ્યમપદલોપી સમાસ' ની વ્યાખ્યા આપો.",
                    backAnswer = "જે સમાસમાં પૂર્વપદ અને ઉત્તરપદ વચ્ચે રહેલા સંબંધ દર્શાવતા મધ્યમ પદોનો વિગ્રહ વખતે લોપ થતો હોય. ઉદાહરણ: ઘોડાગાડી (ઘોડા વડે ચાલતી ગાડી).",
                    category = "સમાસ"
                ),
                FlashcardItem(
                    id = "fc_g4",
                    frontQuestion = "રૂઢિપ્રયોગનો અર્થ: 'આકાશ-પાતાળ એક કરવા'",
                    backAnswer = "ખૂબ જ સખત અને તનતોડ મહેનત કરવી / શક્ય તેટલા તમામ પ્રયાસો કરી છૂટવું.",
                    category = "રૂઢિપ્રયોગ"
                )
            )
        )
    )

    // Pre-curated official blueprints for mock exam generation
    fun generateStandardMockPaper(config: MockPaperConfig): GeneratedMockPaper {
        val isGseb = config.board.contains("GSEB", ignoreCase = true)
        val isClass10 = config.classLevel.contains("10")
        val isMath = config.subject.contains("Math", ignoreCase = true)

        val secAQuestions = listOf(
            MockPaperQuestion(
                number = 1,
                section = "Section A",
                question = if (isMath) "If HCF(306, 657) = 9, what is the LCM(306, 657)?" else "Which gas is liberated when zinc reacts with dilute sulfuric acid?",
                options = if (isMath) listOf("22338", "21450", "22500", "19800") else listOf("Hydrogen (H₂)", "Oxygen (O₂)", "Carbon dioxide (CO₂)", "Nitrogen (N₂)"),
                marks = 1,
                answerKeyOrSolution = if (isMath) "Option A (22338). Formula: LCM = (a × b) / HCF = (306 × 657) / 9 = 22338." else "Option A (Hydrogen). Reaction: Zn + H₂SO₄ → ZnSO₄ + H₂ ↑.",
                markingSchemeStep = "1 mark for correct option and answer identification."
            ),
            MockPaperQuestion(
                number = 2,
                section = "Section A",
                question = if (isMath) "The discriminant of the quadratic equation 2x² - 4x + 3 = 0 is:" else "Which part of the human eye controls the amount of light entering it?",
                options = if (isMath) listOf("-8", "8", "-16", "16") else listOf("Pupil / Iris", "Cornea", "Retina", "Ciliary muscles"),
                marks = 1,
                answerKeyOrSolution = if (isMath) "Option A (-8). D = b² - 4ac = (-4)² - 4(2)(3) = 16 - 24 = -8." else "Option A (Pupil / Iris regulates pupil size).",
                markingSchemeStep = "1 mark for direct formula application."
            ),
            MockPaperQuestion(
                number = 3,
                section = "Section A",
                question = if (isMath) "State True or False: All congruent triangles are similar, but all similar triangles need not be congruent." else "State True or False: Rusting of iron is an endothermic chemical combination reaction.",
                options = listOf("True", "False"),
                marks = 1,
                answerKeyOrSolution = if (isMath) "True. Congruent triangles have equal shape and size (ratio 1:1), hence are always similar." else "False. Rusting is an exothermic oxidation reaction.",
                markingSchemeStep = "1 mark for correct True/False determination with reasoning."
            ),
            MockPaperQuestion(
                number = 4,
                section = "Section A",
                question = if (isMath) "Evaluate: sin² 30° + cos² 30° = ?" else "What is the SI unit of electric resistivity?",
                options = if (isMath) listOf("1", "0", "1/2", "√3/2") else listOf("Ohm-meter (Ω·m)", "Ohm (Ω)", "Ampere (A)", "Volt (V)"),
                marks = 1,
                answerKeyOrSolution = if (isMath) "1. Trigonometric identity sin²θ + cos²θ = 1 for any angle θ." else "Ohm-meter (Ω·m). Derived from R = ρ(L/A) ⇒ ρ = RA/L.",
                markingSchemeStep = "1 mark for accurate unit or evaluated value."
            )
        )

        val secBQuestions = listOf(
            MockPaperQuestion(
                number = 5,
                section = "Section B",
                question = if (isMath) "Find the zeroes of the quadratic polynomial x² + 7x + 10 and verify the relationship between zeroes and coefficients." else "Differentiate between an Exothermic reaction and an Endothermic reaction with one balanced equation each.",
                options = null,
                marks = 2,
                answerKeyOrSolution = if (isMath) """
                    Factoring: x² + 7x + 10 = (x + 2)(x + 5) = 0.
                    Zeroes are α = -2, β = -5.
                    Verification:
                    Sum of zeroes α + β = (-2) + (-5) = -7 = -b/a (-7/1).
                    Product of zeroes αβ = (-2)(-5) = 10 = c/a (10/1). Verified.
                """.trimIndent() else """
                    1. Exothermic: Heat is released. Example: CH₄ + 2O₂ → CO₂ + 2H₂O + Heat.
                    2. Endothermic: Heat is absorbed. Example: CaCO₃ --[Heat]--> CaO + CO₂.
                """.trimIndent(),
                markingSchemeStep = "1 mark for calculating zeroes/definitions; 1 mark for verification/equations."
            ),
            MockPaperQuestion(
                number = 6,
                section = "Section B",
                question = if (isMath) "Find the 20th term from the last term of the AP: 3, 8, 13, ..., 253." else "An electric lamp of 100 Ω, a toaster of 50 Ω, and a water filter of 500 Ω are connected in parallel to a 220 V source. What is the resistance of an electric iron connected to the same source that takes as much current as all three appliances?",
                options = null,
                marks = 2,
                answerKeyOrSolution = if (isMath) """
                    Reverse AP from end: a = 253, common difference d' = -5.
                    Formula: a₂₀ = a + (20 - 1)d'
                    a₂₀ = 253 + 19(-5) = 253 - 95 = 158.
                """.trimIndent() else """
                    Equivalent parallel resistance 1/Rₚ = 1/100 + 1/50 + 1/500
                    1/Rₚ = (5 + 10 + 1) / 500 = 16 / 500 = 4 / 125
                    Rₚ = 125 / 4 = 31.25 Ω.
                    Resistance of electric iron = 31.25 Ω.
                """.trimIndent(),
                markingSchemeStep = "1 mark for formula formulation; 1 mark for numerical substitution and result."
            )
        )

        val secCQuestions = listOf(
            MockPaperQuestion(
                number = 7,
                section = "Section C",
                question = if (isMath) "Prove that (sin θ - 2 sin³ θ) / (2 cos³ θ - cos θ) = tan θ." else "Explain the structure and functioning of a Nephron in the human kidney with a clean labeled diagram explanation.",
                options = null,
                marks = 3,
                answerKeyOrSolution = if (isMath) """
                    LHS = sin θ(1 - 2 sin² θ) / [cos θ(2 cos² θ - 1)]
                    Recall that cos 2θ = 1 - 2 sin² θ = 2 cos² θ - 1
                    Or rewrite: 1 - 2 sin² θ = (sin² θ + cos² θ) - 2 sin² θ = cos² θ - sin² θ
                    And 2 cos² θ - 1 = 2 cos² θ - (sin² θ + cos² θ) = cos² θ - sin² θ
                    Therefore LHS = (sin θ / cos θ) × [(cos² θ - sin² θ) / (cos² θ - sin² θ)]
                    = tan θ = RHS. Hence proved.
                """.trimIndent() else """
                    1. Bowman's capsule: Encloses glomerulus where high blood pressure causes ultrafiltration of urea, glucose, salts.
                    2. Renal Tubule: Selective reabsorption of useful substances (glucose, amino acids, major water) back into capillary network.
                    3. Collecting Duct: Collects concentrated urine and passes to ureter.
                """.trimIndent(),
                markingSchemeStep = "1 mark for factorizing numerator/structural breakdown; 1 mark for substitution/filtration step; 1 mark for concluding proof/excretion flow."
            )
        )

        val secDQuestions = listOf(
            MockPaperQuestion(
                number = 8,
                section = "Section D",
                question = if (isMath) "State and Prove Basic Proportionality Theorem (Thales Theorem)." else "Explain the extraction of metals of medium reactivity from their carbonate and sulfide ores with calcination and roasting equations.",
                options = null,
                marks = 5,
                answerKeyOrSolution = if (isMath) """
                    Statement: If a line is drawn parallel to one side of a triangle intersecting other two sides, then it divides the sides in same ratio (1 Mark).
                    Given & Figure: △ABC with DE ∥ BC intersecting AB at D, AC at E (1 Mark).
                    Construction: Join BE, CD and draw DM ⊥ AC, EN ⊥ AB (1 Mark).
                    Proof:
                    Area(△ADE) = 1/2 × AD × EN; Area(△BDE) = 1/2 × DB × EN
                    ⇒ Area(△ADE)/Area(△BDE) = AD/DB -- (Equation 1)
                    Similarly, Area(△ADE)/Area(△CDE) = AE/EC -- (Equation 2)
                    Triangles BDE and CDE are on same base DE and between same parallels DE and BC
                    ⇒ Area(△BDE) = Area(△CDE) -- (Equation 3)
                    From (1), (2), and (3): AD/DB = AE/EC. (2 Marks). Proved.
                """.trimIndent() else """
                    1. Roasting (for sulfide ores): Heating strongly in presence of excess air.
                       2ZnS + 3O₂ --[Heat]--> 2ZnO + 2SO₂ ↑ (2 Marks).
                    2. Calcination (for carbonate ores): Heating strongly in limited air.
                       ZnCO₃ --[Heat]--> ZnO + CO₂ ↑ (2 Marks).
                    3. Reduction with Carbon: ZnO + C → Zn + CO (1 Mark).
                """.trimIndent(),
                markingSchemeStep = "1 mark for statement/definition; 2 marks for diagrams/equations; 2 marks for derivation steps."
            )
        )

        val sections = listOf(
            MockPaperSection(
                name = "Section A",
                title = "Objective & MCQs",
                marksPerQuestion = 1,
                totalSectionMarks = secAQuestions.sumOf { it.marks },
                instructions = "All questions are compulsory. Choose the single most appropriate option.",
                questions = secAQuestions
            ),
            MockPaperSection(
                name = "Section B",
                title = "Short Answer Questions",
                marksPerQuestion = 2,
                totalSectionMarks = secBQuestions.sumOf { it.marks },
                instructions = "Answer each question concisely in about 30–50 words with necessary calculation steps.",
                questions = secBQuestions
            ),
            MockPaperSection(
                name = "Section C",
                title = "Long Analytical Questions",
                marksPerQuestion = 3,
                totalSectionMarks = secCQuestions.sumOf { it.marks },
                instructions = "Show detailed step-by-step mathematical working or labeled explanation in about 60–80 words.",
                questions = secCQuestions
            ),
            MockPaperSection(
                name = "Section D",
                title = "Core Theorems & Comprehensive Problems",
                marksPerQuestion = 5,
                totalSectionMarks = secDQuestions.sumOf { it.marks },
                instructions = "State theorem, draw proper geometric figures/diagrams, and give complete proofs/reactions.",
                questions = secDQuestions
            )
        )

        val totalCalculatedMarks = sections.sumOf { it.totalSectionMarks }

        return GeneratedMockPaper(
            id = "mock_${System.currentTimeMillis()}",
            title = "${config.board} ${config.classLevel} ${config.subject} Mock Practice Paper",
            board = config.board,
            classLevel = config.classLevel,
            subject = config.subject,
            totalMarks = totalCalculatedMarks,
            durationMinutes = config.durationMinutes,
            generalInstructions = listOf(
                "Total Duration: ${config.durationMinutes} Minutes. Total Marks: $totalCalculatedMarks.",
                "Question paper contains 4 Sections: A (Objective), B (Short Answer), C (Long Answer), and D (Theorems / Comprehensive).",
                "Use of calculators is strictly prohibited in board examination simulations.",
                "All rough work should be done on the blank sheet provided and clearly cancelled.",
                "Write clear question numbers corresponding to the question paper."
            ),
            sections = sections
        )
    }
}
