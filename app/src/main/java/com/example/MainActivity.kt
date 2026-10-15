package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.local.EduSphereDatabase
import com.example.data.local.EduSpherePreferences
import com.example.data.model.ChapterItem
import com.example.data.model.UserProfile
import com.example.data.repository.EduSphereRepository
import com.example.feature.ai.AiAssistantSheet
import com.example.feature.ai.EduSphereTtsManager
import com.example.feature.auth.OnboardingScreen
import com.example.feature.auth.ProfileSetupScreen
import com.example.feature.auth.SignInScreen
import com.example.feature.home.HomeScreen
import com.example.feature.learn.LearnScreen
import com.example.feature.library.LibraryScreen
import com.example.feature.practice.PracticeScreen
import com.example.feature.profile.ProfileScreen
import com.example.ui.theme.EduSphereTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: EduSphereTtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsManager = EduSphereTtsManager(this)

        setContent {
            val context = LocalContext.current
            val preferences = remember { EduSpherePreferences(context) }
            val themeMode by preferences.themeModeFlow.collectAsState(initial = "SYSTEM")

            val isDarkTheme = when (themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }

            EduSphereTheme(darkTheme = isDarkTheme) {
                EduSphereApp(
                    ttsManager = ttsManager,
                    preferences = preferences
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object ShowOnboarding : AuthState()
    object Unauthenticated : AuthState()
    object NeedsProfileSetup : AuthState()
    data class Authenticated(val profile: UserProfile) : AuthState()
}

@Composable
fun EduSphereApp(
    ttsManager: EduSphereTtsManager,
    preferences: EduSpherePreferences
) {
    val context = LocalContext.current
    val repository = remember {
        EduSphereRepository(
            FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
        )
    }
    val database = remember { EduSphereDatabase.getDatabase(context) }

    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var hasCompletedOnboarding by remember { mutableStateOf(false) }
    var isCheckingSession by remember { mutableStateOf(true) }

    // Listen to Firebase Auth state
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            isCheckingSession = false
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    // Observe user profile in real-time when authenticated
    if (currentUser != null) {
        val observedProfile by repository.observeUserProfile(currentUser!!.uid).collectAsState(initial = null)
        userProfile = observedProfile
    } else {
        userProfile = null
    }

    if (isCheckingSession) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
        return
    }

    // Composable Auth Gate
    if (currentUser == null) {
        if (!hasCompletedOnboarding) {
            OnboardingScreen(
                onFinishOnboarding = { hasCompletedOnboarding = true }
            )
        } else {
            SignInScreen(
                onAuthSuccess = {
                    currentUser = Firebase.auth.currentUser
                }
            )
        }
    } else {
        // User is signed in. Check if student profile setup is completed.
        if (userProfile == null || !userProfile!!.onboardingCompleted) {
            ProfileSetupScreen(
                repository = repository,
                onProfileConfigured = {
                    // Profile configured, proceeds to MainContent
                }
            )
        } else {
            MainAppScaffold(
                profile = userProfile,
                repository = repository,
                database = database,
                preferences = preferences,
                ttsManager = ttsManager,
                onSignOut = {
                    currentUser = null
                    userProfile = null
                }
            )
        }
    }
}

@Composable
fun MainAppScaffold(
    profile: UserProfile?,
    repository: EduSphereRepository,
    database: EduSphereDatabase,
    preferences: EduSpherePreferences,
    ttsManager: EduSphereTtsManager,
    onSignOut: () -> Unit
) {
    var selectedBottomTab by remember { mutableIntStateOf(0) }
    var showAiAssistantSheet by remember { mutableStateOf(false) }
    var aiInitialPrompt by remember { mutableStateOf<String?>(null) }
    var chapterQuizToStart by remember { mutableStateOf<ChapterItem?>(null) }

    val navItems = listOf(
        NavigationItemData("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavigationItemData("Library", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_library"),
        NavigationItemData("Practice", Icons.Filled.Quiz, Icons.Outlined.Quiz, "nav_practice"),
        NavigationItemData("Learn", Icons.Filled.School, Icons.Outlined.School, "nav_learn"),
        NavigationItemData("Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedBottomTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedBottomTab = index
                            chapterQuizToStart = null
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    aiInitialPrompt = null
                    showAiAssistantSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("ai_assistant_fab")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Gemini AI Assistant")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedBottomTab) {
                0 -> HomeScreen(
                    profile = profile,
                    onNavigateToLibrary = { selectedBottomTab = 1 },
                    onNavigateToPractice = { selectedBottomTab = 2 },
                    onNavigateToLearn = { selectedBottomTab = 3 },
                    onOpenAiAssistant = { prompt ->
                        aiInitialPrompt = prompt
                        showAiAssistantSheet = true
                    }
                )
                1 -> LibraryScreen(
                    repository = repository,
                    database = database,
                    ttsManager = ttsManager,
                    onStartChapterQuiz = { chapter ->
                        chapterQuizToStart = chapter
                        selectedBottomTab = 2 // Switch to Practice tab
                    }
                )
                2 -> PracticeScreen(
                    repository = repository,
                    initialQuizQuestions = chapterQuizToStart?.sampleQuestions,
                    initialQuizTitle = chapterQuizToStart?.let { "${it.title} Quiz" }
                )
                3 -> LearnScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
                4 -> ProfileScreen(
                    profile = profile,
                    repository = repository,
                    database = database,
                    preferences = preferences,
                    onSignOutSuccess = onSignOut
                )
            }
        }
    }

    if (showAiAssistantSheet) {
        AiAssistantSheet(
            initialPrompt = aiInitialPrompt,
            ttsManager = ttsManager,
            onDismiss = { showAiAssistantSheet = false }
        )
    }
}

data class NavigationItemData(
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
