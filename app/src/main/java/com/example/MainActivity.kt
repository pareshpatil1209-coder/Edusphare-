package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.EduSphereRepository
import com.example.feature.ai.AiStudyAssistantService
import com.example.feature.auth.AuthScreen
import com.example.feature.home.HomeScreen
import com.example.feature.learn.LearnScreen
import com.example.feature.library.LibraryScreen
import com.example.feature.practice.PracticeScreen
import com.example.feature.profile.ProfileScreen
import com.example.ui.theme.EduSphereTheme
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduSphereTheme {
                EduSphereApp()
            }
        }
    }
}

enum class ScreenNav(val label: String) {
    HOME("Home"),
    LIBRARY("Library"),
    PRACTICE("Practice"),
    LEARN("Courses"),
    PROFILE("Profile"),
    AUTH("Sign In")
}

@Composable
fun EduSphereApp() {
    val context = LocalContext.current
    val repository = remember { EduSphereRepository(context) }
    val aiService = remember { AiStudyAssistantService() }

    val userProfile by repository.currentUserProfile.collectAsState()
    var currentScreen by remember { mutableStateOf(ScreenNav.HOME) }

    // Global AI Assistant Dialog State
    var showAiAssistantDialog by remember { mutableStateOf(false) }
    var initialAiPrompt by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = currentScreen != ScreenNav.HOME) {
        currentScreen = ScreenNav.HOME
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            if (currentScreen != ScreenNav.AUTH) {
                NavigationBar(
                    modifier = Modifier.testTag("main_bottom_nav"),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    NavigationBarItem(
                        selected = currentScreen == ScreenNav.HOME,
                        onClick = { currentScreen = ScreenNav.HOME },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        modifier = Modifier.testTag("nav_item_home")
                    )
                    NavigationBarItem(
                        selected = currentScreen == ScreenNav.LIBRARY,
                        onClick = { currentScreen = ScreenNav.LIBRARY },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Library") },
                        label = { Text("Library") },
                        modifier = Modifier.testTag("nav_item_library")
                    )
                    NavigationBarItem(
                        selected = currentScreen == ScreenNav.PRACTICE,
                        onClick = { currentScreen = ScreenNav.PRACTICE },
                        icon = { Icon(Icons.Default.Quiz, contentDescription = "Practice") },
                        label = { Text("Practice") },
                        modifier = Modifier.testTag("nav_item_practice")
                    )
                    NavigationBarItem(
                        selected = currentScreen == ScreenNav.LEARN,
                        onClick = { currentScreen = ScreenNav.LEARN },
                        icon = { Icon(Icons.Default.School, contentDescription = "Learn") },
                        label = { Text("Courses") },
                        modifier = Modifier.testTag("nav_item_learn")
                    )
                    NavigationBarItem(
                        selected = currentScreen == ScreenNav.PROFILE,
                        onClick = { currentScreen = ScreenNav.PROFILE },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentScreen != ScreenNav.AUTH) {
                FloatingActionButton(
                    onClick = {
                        initialAiPrompt = null
                        showAiAssistantDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("fab_ai_assistant")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Study Assistant")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                ScreenNav.HOME -> HomeScreen(
                    profile = userProfile,
                    onNavigateToLibrary = { currentScreen = ScreenNav.LIBRARY },
                    onNavigateToPractice = { currentScreen = ScreenNav.PRACTICE },
                    onNavigateToLearn = { currentScreen = ScreenNav.LEARN },
                    onOpenAiAssistant = { prompt ->
                        initialAiPrompt = prompt
                        showAiAssistantDialog = true
                    }
                )

                ScreenNav.LIBRARY -> LibraryScreen(
                    repository = repository,
                    onNavigateBack = { currentScreen = ScreenNav.HOME }
                )

                ScreenNav.PRACTICE -> PracticeScreen(
                    repository = repository,
                    aiService = aiService
                )

                ScreenNav.LEARN -> LearnScreen(
                    repository = repository
                )

                ScreenNav.PROFILE -> ProfileScreen(
                    repository = repository,
                    onNavigateToAuth = { currentScreen = ScreenNav.AUTH }
                )

                ScreenNav.AUTH -> AuthScreen(
                    repository = repository,
                    onAuthSuccess = { currentScreen = ScreenNav.HOME }
                )
            }
        }
    }

    if (showAiAssistantDialog) {
        AiAssistantModalDialog(
            aiService = aiService,
            initialPrompt = initialAiPrompt,
            onDismiss = { showAiAssistantDialog = false }
        )
    }
}

@Composable
fun AiAssistantModalDialog(
    aiService: AiStudyAssistantService,
    initialPrompt: String?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var promptText by remember { mutableStateOf(initialPrompt ?: "") }
    var responseText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gemini AI Study Assistant", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ask any academic doubt, formula derivation, or study tip grounded in GSEB and CBSE curriculum.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = { Text("e.g. Explain Euclid's Division Lemma simply") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (promptText.isNotBlank()) {
                                    isLoading = true
                                    scope.launch {
                                        val answer = aiService.askAssistant(promptText)
                                        responseText = answer
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading && promptText.isNotBlank()
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                )

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Gemini is analyzing...", style = MaterialTheme.typography.bodySmall)
                    }
                }

                if (responseText != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Explanation:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                IconButton(onClick = {
                                    tts?.speak(responseText!!, TextToSpeech.QUEUE_FLUSH, null, "ai_explanation")
                                }) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = responseText!!,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
