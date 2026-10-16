package com.example.feature.practice

import android.content.Intent
import android.speech.tts.TextToSpeech
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.FlashcardDeck
import com.example.data.model.FlashcardItem
import com.example.data.model.GeneratedMockPaper
import com.example.data.model.MockPaperConfig
import com.example.data.model.QuizAttempt
import com.example.data.model.QuizQuestion
import com.example.data.repository.EduSphereRepository
import com.example.feature.ai.AiStudyAssistantService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    repository: EduSphereRepository,
    aiService: AiStudyAssistantService
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }

    // TTS
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Active Mock Paper View state
    var currentGeneratedPaper by remember { mutableStateOf<GeneratedMockPaper?>(null) }

    if (currentGeneratedPaper != null) {
        BackHandler { currentGeneratedPaper = null }
        GeneratedMockPaperDetailView(
            paper = currentGeneratedPaper!!,
            onBack = { currentGeneratedPaper = null },
            onSave = {
                scope.launch {
                    repository.saveMockPaper(currentGeneratedPaper!!)
                }
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("practice_screen")
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("AI Mock Papers") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Flashcard Studio") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Timed Quizzes") }
            )
        }

        when (selectedTab) {
            0 -> MockPaperGeneratorTab(
                aiService = aiService,
                onPaperGenerated = { currentGeneratedPaper = it }
            )
            1 -> FlashcardStudioTab(
                aiService = aiService,
                tts = tts
            )
            2 -> TimedQuizTab(
                aiService = aiService,
                repository = repository
            )
        }
    }
}

@Composable
fun MockPaperGeneratorTab(
    aiService: AiStudyAssistantService,
    onPaperGenerated: (GeneratedMockPaper) -> Unit
) {
    val scope = rememberCoroutineScope()

    var board by remember { mutableStateOf("GSEB") }
    var classLevel by remember { mutableStateOf("Class 10") }
    var subject by remember { mutableStateOf("Mathematics") }
    var language by remember { mutableStateOf("English") }
    var totalMarks by remember { mutableIntStateOf(50) }
    var durationMinutes by remember { mutableIntStateOf(90) }
    var difficulty by remember { mutableStateOf("Standard") }
    var includeMCQ by remember { mutableStateOf(true) }
    var includeShortAnswer by remember { mutableStateOf(true) }
    var includeLongAnswer by remember { mutableStateOf(true) }

    var isGenerating by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "AI Mock Paper Generator",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Generate syllabus-aligned exam practice papers with blueprint marking schemes.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "1. Exam Board", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("GSEB", "CBSE").forEach { b ->
                            FilterChip(
                                selected = board == b,
                                onClick = { board = b },
                                label = { Text(b) }
                            )
                        }
                    }

                    Text(text = "2. Class Level", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Class 9", "Class 10", "Class 12 Science").forEach { c ->
                            FilterChip(
                                selected = classLevel == c,
                                onClick = { classLevel = c },
                                label = { Text(c) }
                            )
                        }
                    }

                    Text(text = "3. Subject", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Mathematics", "Science & Tech", "Physics").forEach { s ->
                            FilterChip(
                                selected = subject == s,
                                onClick = { subject = s },
                                label = { Text(s) }
                            )
                        }
                    }

                    Text(text = "4. Total Marks & Duration", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(25 to 45, 50 to 90, 80 to 180).forEach { (m, d) ->
                            FilterChip(
                                selected = totalMarks == m,
                                onClick = {
                                    totalMarks = m
                                    durationMinutes = d
                                },
                                label = { Text("$m Marks ($d min)") }
                            )
                        }
                    }

                    Text(text = "5. Question Types", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = includeMCQ,
                            onClick = { includeMCQ = !includeMCQ },
                            label = { Text("MCQs") }
                        )
                        FilterChip(
                            selected = includeShortAnswer,
                            onClick = { includeShortAnswer = !includeShortAnswer },
                            label = { Text("Short Answer") }
                        )
                        FilterChip(
                            selected = includeLongAnswer,
                            onClick = { includeLongAnswer = !includeLongAnswer },
                            label = { Text("Long / Proofs") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            isGenerating = true
                            scope.launch {
                                val config = MockPaperConfig(
                                    board = board,
                                    classLevel = classLevel,
                                    subject = subject,
                                    language = language,
                                    totalMarks = totalMarks,
                                    durationMinutes = durationMinutes,
                                    difficulty = difficulty,
                                    includeMCQ = includeMCQ,
                                    includeShortAnswer = includeShortAnswer,
                                    includeLongAnswer = includeLongAnswer
                                )
                                val paper = aiService.generateMockPaper(config)
                                isGenerating = false
                                onPaperGenerated(paper)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_mock_paper_button"),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isGenerating
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Generating with Gemini AI...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Board Mock Paper")
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ AI Practice Notice",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "All mock papers are synthetically generated using verified board syllabus weightage. The answer keys remain hidden until you choose to reveal them.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratedMockPaperDetailView(
    paper: GeneratedMockPaper,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    var showMarkingScheme by remember { mutableStateOf(false) }
    var isExamTimerRunning by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(paper.durationMinutes * 60) }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(isExamTimerRunning) {
        while (isExamTimerRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(paper.title, maxLines = 1, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, paper.title)
                            putExtra(Intent.EXTRA_TEXT, "${paper.title}\nTotal Marks: ${paper.totalMarks}\nDuration: ${paper.durationMinutes} min\n\nGenerated via EduSphere AI Practice Platform.")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Export Mock Paper"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Export Paper")
                    }

                    IconButton(onClick = {
                        onSave()
                        isSaved = true
                    }) {
                        Icon(
                            if (isSaved) Icons.Default.CheckCircle else Icons.Default.Download,
                            contentDescription = "Save Paper",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exam Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${paper.board} • ${paper.classLevel}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Max Marks: ${paper.totalMarks}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = paper.subject,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Time Allowed: ${paper.durationMinutes} Minutes",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Timer Controller
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val mins = remainingSeconds / 60
                            val secs = remainingSeconds % 60
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Button(
                                onClick = { isExamTimerRunning = !isExamTimerRunning },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (isExamTimerRunning) "Pause Exam" else "Start Timed Exam")
                            }
                        }
                    }
                }
            }

            // General Instructions
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "General Instructions:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        paper.generalInstructions.forEach { inst ->
                            Text(
                                text = "• $inst",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Questions Sections
            paper.sections.forEach { section ->
                item {
                    Text(
                        text = "${section.sectionName} — ${section.totalMarks} Marks",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = section.instructions,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(section.questions) { q ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Q${q.questionNumber}.",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "[${q.marks} Mark${if (q.marks > 1) "s" else ""}]",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = q.prompt,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )

                            if (!q.options.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                q.options.forEachIndexed { idx, opt ->
                                    val optLetter = ('A' + idx)
                                    Text(
                                        text = "($optLetter) $opt",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Marking Scheme Toggle & View
            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { showMarkingScheme = !showMarkingScheme },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        if (showMarkingScheme) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showMarkingScheme) "Hide Marking Scheme & Answer Key" else "Reveal Marking Scheme & Answer Key")
                }
            }

            if (showMarkingScheme) {
                item {
                    Text(
                        text = "Official Marking Scheme & Step Solutions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                items(paper.markingScheme) { answer ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Answer to Q${answer.questionNumber}:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = answer.expectedAnswer,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Step Breakdown: ${answer.stepByStepMarking}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun FlashcardStudioTab(
    aiService: AiStudyAssistantService,
    tts: TextToSpeech?
) {
    val scope = rememberCoroutineScope()
    var currentCards by remember { mutableStateOf(AuthorizedEducationalCatalog.sampleFlashcardDecks[0].cards) }
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var customTopicPrompt by remember { mutableStateOf("") }
    var isGeneratingFlashcards by remember { mutableStateOf(false) }

    val currentCard = currentCards.getOrNull(currentCardIndex)

    val cardRotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flashcard_rotation"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Flashcard Studio",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Master formulas, theorems, and vocabulary with 3D flip review.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Active Flashcard Container
        if (currentCard != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .graphicsLayer {
                            rotationY = cardRotation
                            cameraDistance = 12f * density
                        }
                        .clickable {
                            isFlipped = !isFlipped
                            showHint = false
                        }
                        .testTag("flashcard_3d_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (cardRotation <= 90f) "QUESTION / PROMPT" else "ANSWER / FORMULA",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (cardRotation <= 90f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (cardRotation <= 90f) currentCard.front else currentCard.back,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.graphicsLayer {
                                    // Invert text horizontally when flipped so it reads correctly
                                    if (cardRotation > 90f) rotationY = 180f
                                }
                            )

                            if (showHint && currentCard.hint != null && cardRotation <= 90f) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "💡 Hint: ${currentCard.hint}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }

            // Card Navigation & Action Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (currentCardIndex > 0) {
                            currentCardIndex--
                            isFlipped = false
                            showHint = false
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Card")
                    }

                    Text(
                        text = "Card ${currentCardIndex + 1} of ${currentCards.size}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    // TTS button
                    IconButton(onClick = {
                        val textToSpeak = if (isFlipped) currentCard.back else currentCard.front
                        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "card_tts")
                    }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Listen")
                    }

                    // Hint button
                    IconButton(onClick = { showHint = !showHint }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Hint")
                    }

                    IconButton(onClick = {
                        if (currentCardIndex < currentCards.size - 1) {
                            currentCardIndex++
                            isFlipped = false
                            showHint = false
                        }
                    }) {
                        Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Next Card")
                    }
                }
            }
        }

        // Generate AI Flashcard Set
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Generate AI Study Cards",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customTopicPrompt,
                        onValueChange = { customTopicPrompt = it },
                        placeholder = { Text("e.g., Trigonometry Identities, Ohm's Law, Sandhi") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (customTopicPrompt.isNotBlank()) {
                                isGeneratingFlashcards = true
                                scope.launch {
                                    val newCards = aiService.generateFlashcards(customTopicPrompt)
                                    currentCards = newCards
                                    currentCardIndex = 0
                                    isFlipped = false
                                    isGeneratingFlashcards = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isGeneratingFlashcards
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isGeneratingFlashcards) "Generating Cards..." else "Generate Study Deck")
                    }
                }
            }
        }
    }
}

@Composable
fun TimedQuizTab(
    aiService: AiStudyAssistantService,
    repository: EduSphereRepository
) {
    val scope = rememberCoroutineScope()
    var quizTopic by remember { mutableStateOf("Real Numbers") }
    var questions by remember {
        mutableStateOf(
            listOf(
                QuizQuestion(
                    id = "q1",
                    question = "What is the HCF of two prime numbers a and b?",
                    options = listOf("0", "1", "a × b", "a + b"),
                    correctIndex = 1,
                    explanation = "Prime numbers share no common factors other than 1.",
                    chapter = "Real Numbers"
                ),
                QuizQuestion(
                    id = "q2",
                    question = "If α and β are zeroes of ax² + bx + c, then α + β is:",
                    options = listOf("b/a", "-b/a", "c/a", "-c/a"),
                    correctIndex = 1,
                    explanation = "Sum of zeroes = -(coefficient of x) / (coefficient of x²) = -b/a.",
                    chapter = "Polynomials"
                ),
                QuizQuestion(
                    id = "q3",
                    question = "Which law states that mass cannot be created or destroyed in a reaction?",
                    options = listOf("Law of Constant Proportions", "Law of Conservation of Mass", "Avogadro's Law", "Boyle's Law"),
                    correctIndex = 1,
                    explanation = "Lavoisier formulated the Law of Conservation of Mass.",
                    chapter = "Chemical Reactions"
                )
            )
        )
    }

    var currentQuestionIdx by remember { mutableIntStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions.getOrNull(currentQuestionIdx)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Interactive Timed Quizzes",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Test your board exam preparation with validated scoring & answer explanations.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (quizCompleted) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 Quiz Completed!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Your Score: $score / ${questions.size}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        val pct = (score.toFloat() / questions.size.toFloat()) * 100f
                        Text(
                            text = "${pct.toInt()}% Accuracy Recorded to Profile Report",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                currentQuestionIdx = 0
                                selectedOptionIdx = null
                                isSubmitted = false
                                score = 0
                                quizCompleted = false
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Retake Quiz")
                        }
                    }
                }
            }
        } else if (currentQ != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${currentQuestionIdx + 1} of ${questions.size}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Topic: ${currentQ.chapter}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentQ.question,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        currentQ.options.forEachIndexed { idx, opt ->
                            val isSelected = selectedOptionIdx == idx
                            val isCorrect = idx == currentQ.correctIndex

                            val backgroundColor = when {
                                isSubmitted && isCorrect -> Color(0xFFD1FAE5)
                                isSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = !isSubmitted) { selectedOptionIdx = idx },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = backgroundColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${('A' + idx)}. $opt",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        }

                        if (isSubmitted) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Explanation:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = currentQ.explanation,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!isSubmitted) {
                            Button(
                                onClick = {
                                    if (selectedOptionIdx != null) {
                                        isSubmitted = true
                                        if (selectedOptionIdx == currentQ.correctIndex) {
                                            score++
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.End),
                                enabled = selectedOptionIdx != null,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Submit Answer")
                            }
                        } else {
                            Button(
                                onClick = {
                                    if (currentQuestionIdx < questions.size - 1) {
                                        currentQuestionIdx++
                                        selectedOptionIdx = null
                                        isSubmitted = false
                                    } else {
                                        quizCompleted = true
                                        scope.launch {
                                            val attempt = QuizAttempt(
                                                id = UUID.randomUUID().toString(),
                                                quizTitle = "$quizTopic Speed Quiz",
                                                subject = "Core",
                                                score = score,
                                                totalQuestions = questions.size,
                                                percentage = (score.toFloat() / questions.size) * 100f
                                            )
                                            repository.saveQuizResult(attempt)
                                        }
                                    }
                                },
                                modifier = Modifier.align(Alignment.End),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (currentQuestionIdx < questions.size - 1) "Next Question" else "Finish Quiz")
                            }
                        }
                    }
                }
            }
        }
    }
}
