package com.example.feature.practice

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuthorizedEducationalCatalog
import com.example.data.model.EducationalSeedData
import com.example.data.model.FlashcardDeck
import com.example.data.model.FlashcardItem
import com.example.data.model.GeneratedMockPaper
import com.example.data.model.MockPaperConfig
import com.example.data.model.QuestionItem
import com.example.data.model.TestResultItem
import com.example.data.repository.EduSphereRepository
import com.example.feature.ai.AiStudyAssistantService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    repository: EduSphereRepository,
    initialQuizQuestions: List<QuestionItem>? = null,
    initialQuizTitle: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val aiService = remember { AiStudyAssistantService() }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: AI Mock Papers, 1: Flashcards, 2: Quizzes, 3: Analytics

    // Sub-states
    var activeQuizTitle by remember { mutableStateOf(initialQuizTitle) }
    var activeQuizQuestions by remember { mutableStateOf(initialQuizQuestions) }
    var activeMockPaper by remember { mutableStateOf<GeneratedMockPaper?>(null) }
    var activeFlashcardDeck by remember { mutableStateOf<FlashcardDeck?>(null) }

    // Dialogs
    var showMockGeneratorDialog by remember { mutableStateOf(false) }
    var showAiFlashcardDialog by remember { mutableStateOf(false) }
    var isGeneratingAiPaper by remember { mutableStateOf(false) }

    val testResults by repository.observeTestResults().collectAsState(initial = emptyList())

    // Back handlers
    BackHandler(enabled = activeMockPaper != null || activeFlashcardDeck != null || (activeQuizQuestions != null && activeQuizQuestions!!.isNotEmpty())) {
        if (activeQuizQuestions != null && activeQuizQuestions!!.isNotEmpty()) {
            activeQuizQuestions = null
            activeQuizTitle = null
        } else if (activeMockPaper != null) {
            activeMockPaper = null
        } else if (activeFlashcardDeck != null) {
            activeFlashcardDeck = null
        }
    }

    if (activeQuizQuestions != null && activeQuizQuestions!!.isNotEmpty()) {
        ActiveQuizView(
            title = activeQuizTitle ?: "Practice Quiz",
            questions = activeQuizQuestions!!,
            onQuizFinished = { score, totalMarks, accuracy, weakTopics ->
                val item = TestResultItem(
                    userId = "",
                    testTitle = activeQuizTitle ?: "Practice Quiz",
                    subject = "Curriculum Test",
                    score = score,
                    totalMarks = totalMarks,
                    accuracy = accuracy,
                    weakTopics = weakTopics
                )
                coroutineScope.launch {
                    repository.recordTestResult(item)
                    snackbarHostState.showSnackbar("Score saved to your profile analytics!")
                }
            },
            onExitQuiz = {
                activeQuizQuestions = null
                activeQuizTitle = null
            }
        )
    } else if (activeMockPaper != null) {
        MockPaperDetailView(
            paper = activeMockPaper!!,
            onStartPracticeQuiz = { questions, title ->
                activeQuizTitle = title
                activeQuizQuestions = questions
            },
            onBack = { activeMockPaper = null }
        )
    } else if (activeFlashcardDeck != null) {
        InteractiveFlashcardPlayer(
            deck = activeFlashcardDeck!!,
            onExit = { activeFlashcardDeck = null }
        )
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .testTag("practice_screen")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Examination Preparation",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "AI Mock Papers, Flashcard Decks, and Timed Board Quizzes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Mock Papers") },
                        modifier = Modifier.testTag("tab_mock_papers")
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Flashcards") },
                        modifier = Modifier.testTag("tab_flashcards")
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("Quizzes") },
                        modifier = Modifier.testTag("tab_quizzes")
                    )
                    Tab(
                        selected = selectedTabIndex == 3,
                        onClick = { selectedTabIndex = 3 },
                        text = { Text("Analytics (${testResults.size})") },
                        modifier = Modifier.testTag("tab_analytics")
                    )
                }

                when (selectedTabIndex) {
                    0 -> MockPapersTab(
                        onGenerateClick = { showMockGeneratorDialog = true },
                        onSelectPaper = { activeMockPaper = it }
                    )
                    1 -> FlashcardsTab(
                        onOpenDeck = { activeFlashcardDeck = it },
                        onCreateAiDeck = { showAiFlashcardDialog = true }
                    )
                    2 -> QuizzesTab(
                        onStartQuiz = { title, questions ->
                            activeQuizTitle = title
                            activeQuizQuestions = questions
                        }
                    )
                    3 -> AnalyticsTab(testResults = testResults)
                }
            }
        }
    }

    // AI Mock Paper Generator Dialog
    if (showMockGeneratorDialog) {
        MockPaperGeneratorModal(
            isLoading = isGeneratingAiPaper,
            onDismiss = { showMockGeneratorDialog = false },
            onGenerate = { config ->
                isGeneratingAiPaper = true
                coroutineScope.launch {
                    val paper = aiService.generateMockPaper(config)
                    isGeneratingAiPaper = false
                    showMockGeneratorDialog = false
                    activeMockPaper = paper
                }
            }
        )
    }

    // AI Flashcard Generator Dialog
    if (showAiFlashcardDialog) {
        var topicInput by remember { mutableStateOf("") }
        var isGeneratingCards by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { if (!isGeneratingCards) showAiFlashcardDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Flashcard Creator")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Enter any topic or paste study notes. Gemini AI will create a dedicated 5-card revision study set.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("Topic or Study Notes") },
                        placeholder = { Text("e.g. Newton's 3 Laws of Motion or Chemical Bonding") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    if (isGeneratingCards) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Generating smart study cards...", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (topicInput.isNotBlank()) {
                            isGeneratingCards = true
                            coroutineScope.launch {
                                val cards = aiService.generateFlashcards(topicInput)
                                isGeneratingCards = false
                                showAiFlashcardDialog = false
                                activeFlashcardDeck = FlashcardDeck(
                                    id = "ai_deck_${System.currentTimeMillis()}",
                                    title = "AI Deck: ${topicInput.take(24)}...",
                                    board = "Custom",
                                    classLevel = "General",
                                    subject = "Revision",
                                    description = "AI-generated revision study set for $topicInput",
                                    cards = cards
                                )
                            }
                        }
                    },
                    enabled = topicInput.isNotBlank() && !isGeneratingCards
                ) {
                    Text("Generate")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAiFlashcardDialog = false },
                    enabled = !isGeneratingCards
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ----------------- SUB-TABS -----------------

@Composable
fun MockPapersTab(
    onGenerateClick: () -> Unit,
    onSelectPaper: (GeneratedMockPaper) -> Unit
) {
    // Pre-configured official papers remembered at composable scope
    val standardGsebMath = remember {
        AuthorizedEducationalCatalog.generateStandardMockPaper(
            MockPaperConfig(board = "GSEB", classLevel = "Class 10", subject = "Mathematics", totalMarks = 80)
        )
    }
    val standardGsebScience = remember {
        AuthorizedEducationalCatalog.generateStandardMockPaper(
            MockPaperConfig(board = "GSEB", classLevel = "Class 10", subject = "Science", totalMarks = 80)
        )
    }
    val standardCbseMath = remember {
        AuthorizedEducationalCatalog.generateStandardMockPaper(
            MockPaperConfig(board = "CBSE", classLevel = "Class 10", subject = "Mathematics", totalMarks = 80)
        )
    }
    val standardPapers = remember { listOf(standardGsebMath, standardGsebScience, standardCbseMath) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // AI Generator Hero Card
        item {
            ElevatedCard(
                onClick = onGenerateClick,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_mock_paper_generator_button")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Mock Exam Generator 🚀",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Generate custom GSEB/CBSE pattern papers with verified marks & marking schemes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Official Board Blueprints & Mock Papers",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(standardPapers) { paper ->
            Card(
                onClick = { onSelectPaper(paper) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = paper.board,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = paper.classLevel,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "${paper.totalMarks} Marks • ${paper.durationMinutes} Mins",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = paper.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${paper.sections.size} Sections: ${paper.sections.joinToString(" • ") { "${it.name} (${it.totalSectionMarks}M)" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✅ Marking Scheme Included",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "View Paper →",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlashcardsTab(
    onOpenDeck: (FlashcardDeck) -> Unit,
    onCreateAiDeck: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ElevatedCard(
                onClick = onCreateAiDeck,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_flashcard_generator_button")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Style, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Create Flashcards with AI 🎴",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Instant 3D flip study sets from any topic, formula, or custom notes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Curated Board Syllabus Decks",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(AuthorizedEducationalCatalog.flashcardDecks) { deck ->
            Card(
                onClick = { onOpenDeck(deck) },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${deck.board} • ${deck.classLevel}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "${deck.cards.size} Cards",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = deck.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = deck.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Practice Deck →",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun QuizzesTab(
    onStartQuiz: (title: String, questions: List<QuestionItem>) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ElevatedCard(
                onClick = {
                    val allQuestions = EducationalSeedData.chapters.flatMap { it.sampleQuestions }
                    onStartQuiz("All-India Full Syllabus Simulation", allQuestions)
                },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Full Syllabus Mock Examination",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Timed simulation with live countdown, instant scoring & weak-topics analysis",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Chapter-wise Tests",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(EducationalSeedData.chapters) { chapter ->
            Card(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${chapter.subjectId.uppercase()} • Chapter ${chapter.number}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = chapter.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "${chapter.sampleQuestions.size} Questions • 10 Mins",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { onStartQuiz("${chapter.title} Quiz", chapter.sampleQuestions) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Start")
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsTab(testResults: List<TestResultItem>) {
    if (testResults.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No tests recorded yet",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Take a chapter quiz or mock examination above to see your scores, accuracy, and weak topic analytics!",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testResults) { res ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = res.testTitle,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Accuracy: ${res.accuracy}% • ${res.subject}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (res.weakTopics.isNotEmpty()) {
                                Text(
                                    text = "Needs Review: ${res.weakTopics.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(
                                    if (res.accuracy >= 75) Color(0xFF10B981).copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${res.score}/${res.totalMarks}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (res.accuracy >= 75) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------- MODALS & DETAIL VIEWS -----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockPaperDetailView(
    paper: GeneratedMockPaper,
    onStartPracticeQuiz: (List<QuestionItem>, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showMarkingScheme by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(paper.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Export/Share Paper
                    IconButton(onClick = {
                        val shareText = StringBuilder().apply {
                            appendLine("📚 ${paper.title}")
                            appendLine("Board: ${paper.board} | ${paper.classLevel} | ${paper.subject}")
                            appendLine("Total Marks: ${paper.totalMarks} | Duration: ${paper.durationMinutes} Mins")
                            appendLine("\n${paper.disclaimer}\n")
                            appendLine("GENERAL INSTRUCTIONS:")
                            paper.generalInstructions.forEach { appendLine("• $it") }
                            paper.sections.forEach { sec ->
                                appendLine("\n--- ${sec.name}: ${sec.title} (${sec.totalSectionMarks} Marks) ---")
                                sec.questions.forEach { q ->
                                    appendLine("Q${q.number}. ${q.question} [${q.marks}M]")
                                    q.options?.forEachIndexed { i, opt -> appendLine("   (${('A' + i)}) $opt") }
                                    if (showMarkingScheme) {
                                        appendLine("   Answer: ${q.answerKeyOrSolution}")
                                    }
                                }
                            }
                        }.toString()

                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Export Mock Exam Paper"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Export Paper")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // AI Practice Material Disclaimer Banner
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = paper.disclaimer,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Header Meta Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Marks: ${paper.totalMarks}", fontWeight = FontWeight.Bold)
                        Text(text = "Duration: ${paper.durationMinutes} Minutes", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Board & Class: ${paper.board} • ${paper.classLevel}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "General Instructions:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelMedium)
                    paper.generalInstructions.forEach {
                        Text(text = "• $it", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toggle Marking Scheme
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question Paper Sections",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                OutlinedButton(
                    onClick = { showMarkingScheme = !showMarkingScheme },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (showMarkingScheme) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (showMarkingScheme) "Hide Answers" else "Show Solution Key", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sections
            paper.sections.forEach { section ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${section.name}: ${section.title}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${section.totalSectionMarks} Marks",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                section.questions.forEach { q ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Q${q.number}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "[${q.marks} Mark${if (q.marks > 1) "s" else ""}]",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = q.question, style = MaterialTheme.typography.bodyMedium)

                            q.options?.forEachIndexed { i, opt ->
                                Text(
                                    text = "(${('A' + i)}) $opt",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 12.dp, top = 2.dp)
                                )
                            }

                            // Marking Scheme & Answer
                            AnimatedVisibility(visible = showMarkingScheme) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .background(Color(0xFF10B981).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "✓ Solution: ${q.answerKeyOrSolution}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF047857)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Marking Scheme: ${q.markingSchemeStep}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Convert MCQs to Interactive Quiz
            val objectiveQuestions = paper.sections.flatMap { it.questions }
                .filter { it.options != null && it.options.isNotEmpty() }
                .mapIndexed { idx, mq ->
                    QuestionItem(
                        id = "obj_${idx}_${mq.number}",
                        questionText = mq.question,
                        options = mq.options!!,
                        correctIndex = 0, // Option A default in generator
                        explanation = mq.answerKeyOrSolution
                    )
                }

            if (objectiveQuestions.isNotEmpty()) {
                Button(
                    onClick = { onStartPracticeQuiz(objectiveQuestions, "${paper.subject} Live Practice") },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Attempt Section A in Live Timed Quiz (${objectiveQuestions.size} Questions)")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun InteractiveFlashcardPlayer(
    deck: FlashcardDeck,
    onExit: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    val masteredIds = remember { mutableStateListOf<String>() }
    var isCompleted by remember { mutableStateOf(false) }

    val currentCard = deck.cards[currentIndex]
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(400),
        label = "card_flip"
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onExit) {
                    Icon(Icons.Default.Close, contentDescription = "Exit Deck")
                }
                Text(
                    text = deck.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
                Text(
                    text = "${currentIndex + 1}/${deck.cards.size}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        if (isCompleted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(50.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Study Set Complete! 🎉",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mastered ${masteredIds.size} of ${deck.cards.size} Cards",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        currentIndex = 0
                        isFlipped = false
                        isCompleted = false
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Review Deck Again")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                    Text("Return to Flashcard Studio")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / deck.cards.size },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tap Card to Flip",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3D Flip Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable {
                            isFlipped = !isFlipped
                            showHint = false
                        }
                ) {
                    if (rotation <= 90f) {
                        // FRONT
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentCard.category,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = currentCard.frontQuestion,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 32.sp
                                )

                                if (currentCard.hint != null) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    if (showHint) {
                                        Text(
                                            text = "💡 Hint: ${currentCard.hint}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        TextButton(onClick = { showHint = true }) {
                                            Text("Show Hint", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // BACK (mirror rotation fix)
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Explanation & Answer",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = currentCard.backAnswer,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 26.sp
                                )

                                if (currentCard.formulaOrRule != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = currentCard.formulaOrRule!!,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions: Need Review vs Mastered
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isFlipped = false
                            showHint = false
                            if (currentIndex < deck.cards.size - 1) {
                                currentIndex++
                            } else {
                                isCompleted = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Need Review 🔁")
                    }

                    Button(
                        onClick = {
                            if (!masteredIds.contains(currentCard.id)) {
                                masteredIds.add(currentCard.id)
                            }
                            isFlipped = false
                            showHint = false
                            if (currentIndex < deck.cards.size - 1) {
                                currentIndex++
                            } else {
                                isCompleted = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Mastered ✅")
                    }
                }
            }
        }
    }
}

@Composable
fun MockPaperGeneratorModal(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onGenerate: (MockPaperConfig) -> Unit
) {
    var board by remember { mutableStateOf("GSEB") }
    var classLevel by remember { mutableStateOf("Class 10") }
    var subject by remember { mutableStateOf("Mathematics") }
    var language by remember { mutableStateOf("English") }
    var totalMarks by remember { mutableIntStateOf(80) }
    var duration by remember { mutableIntStateOf(180) }
    var difficulty by remember { mutableStateOf("Standard Board") }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Configure AI Mock Paper")
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = "Education Board", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("GSEB", "CBSE", "State Board")) { b ->
                        FilterChip(selected = board == b, onClick = { board = b }, label = { Text(b) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Class & Stream", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("Class 10", "Class 9", "Class 12 Science", "Class 11 Science")) { c ->
                        FilterChip(selected = classLevel == c, onClick = { classLevel = c }, label = { Text(c) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Subject", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("Mathematics", "Science", "Physics", "Chemistry", "Social Science", "Gujarati")) { s ->
                        FilterChip(selected = subject == s, onClick = { subject = s }, label = { Text(s) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Language & Format", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("English", "Gujarati", "Hindi")) { l ->
                        FilterChip(selected = language == l, onClick = { language = l }, label = { Text(l) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Marks & Duration", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf(Triple(25, 45, "25M (Unit Test)"), Triple(50, 90, "50M (Midterm)"), Triple(80, 180, "80M (Board Exam)"))) { (m, d, label) ->
                        FilterChip(
                            selected = totalMarks == m,
                            onClick = {
                                totalMarks = m
                                duration = d
                            },
                            label = { Text(label) }
                        )
                    }
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Drafting verified exam paper...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onGenerate(
                        MockPaperConfig(
                            board = board,
                            classLevel = classLevel,
                            subject = subject,
                            language = language,
                            totalMarks = totalMarks,
                            durationMinutes = duration,
                            difficulty = difficulty
                        )
                    )
                },
                enabled = !isLoading
            ) {
                Text("Generate Mock Paper")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ActiveQuizView(
    title: String,
    questions: List<QuestionItem>,
    onQuizFinished: (score: Int, total: Int, accuracy: Int, weakTopics: List<String>) -> Unit,
    onExitQuiz: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var showResults by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(questions.size * 60) }
    val weakTopics = remember { mutableListOf<String>() }

    LaunchedEffect(showResults) {
        if (!showResults) {
            while (secondsRemaining > 0) {
                delay(1000L)
                secondsRemaining--
            }
            showResults = true
            val acc = if (questions.isNotEmpty()) (correctCount * 100) / questions.size else 0
            onQuizFinished(correctCount, questions.size, acc, weakTopics)
        }
    }

    if (showResults) {
        val accuracy = if (questions.isNotEmpty()) (correctCount * 100) / questions.size else 0
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(if (accuracy >= 60) Color(0xFF10B981).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (accuracy >= 60) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Test Completed!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Score: $correctCount out of ${questions.size} ($accuracy% Accuracy)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Performance Breakdown:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "• Correct Answers: $correctCount", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "• Incorrect / Skipped: ${questions.size - correctCount}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = if (accuracy >= 75) "• Performance: Excellent! Ready for board exam." else "• Performance: Review weak topics and retry.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onExitQuiz,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Return to Practice Hub")
            }
        }
    } else {
        val currentQuestion = questions[currentIndex]

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onExitQuiz) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Quiz")
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        modifier = Modifier.weight(1f, fill = false).padding(horizontal = 8.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HourglassTop, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${secondsRemaining / 60}:${(secondsRemaining % 60).toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Question ${currentIndex + 1} of ${questions.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentQuestion.questionText,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = selectedOption == index
                    val isCorrect = index == currentQuestion.correctIndex

                    val borderColor = when {
                        !isAnswerSubmitted && isSelected -> MaterialTheme.colorScheme.primary
                        isAnswerSubmitted && isCorrect -> Color(0xFF10B981)
                        isAnswerSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }

                    val containerColor = when {
                        !isAnswerSubmitted && isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        isAnswerSubmitted && isCorrect -> Color(0xFF10B981).copy(alpha = 0.15f)
                        isAnswerSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !isAnswerSubmitted) { selectedOption = index },
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(borderColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A' + index).toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = borderColor
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = option, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(visible = isAnswerSubmitted) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption == currentQuestion.correctIndex) Color(0xFF10B981).copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (selectedOption == currentQuestion.correctIndex) "✓ Correct Answer!" else "✗ Incorrect",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOption == currentQuestion.correctIndex) Color(0xFF047857) else MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQuestion.explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isAnswerSubmitted) {
                    Button(
                        onClick = {
                            isAnswerSubmitted = true
                            if (selectedOption == currentQuestion.correctIndex) {
                                correctCount++
                            } else {
                                weakTopics.add(currentQuestion.questionText.take(20))
                            }
                        },
                        enabled = selectedOption != null,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Check Answer")
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                currentIndex++
                                selectedOption = null
                                isAnswerSubmitted = false
                            } else {
                                showResults = true
                                val acc = if (questions.isNotEmpty()) (correctCount * 100) / questions.size else 0
                                onQuizFinished(correctCount, questions.size, acc, weakTopics)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentIndex < questions.size - 1) "Next Question →" else "View Results")
                    }
                }
            }
        }
    }
}
