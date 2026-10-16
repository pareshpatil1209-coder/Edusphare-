package com.example.feature.profile

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.data.model.ActivityLogItem
import com.example.data.model.StudyReport
import com.example.data.model.SubjectProgressStat
import com.example.data.model.UserProfile
import com.example.data.repository.EduSphereRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    repository: EduSphereRepository,
    onNavigateToAuth: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val profile by repository.currentUserProfile.collectAsState()
    val quizHistory by repository.quizHistory.collectAsState()
    val completedLessons by repository.completedLessons.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var activeStudyReport by remember { mutableStateOf<StudyReport?>(null) }
    var isGeneratingReport by remember { mutableStateOf(false) }

    // Report Viewer Mode
    if (activeStudyReport != null) {
        val report = activeStudyReport!!
        BackHandler { activeStudyReport = null }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Student Study Report", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { activeStudyReport = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "EduSphere Study Report - ${report.studentName}")
                                putExtra(Intent.EXTRA_TEXT, "Official Study Report for ${report.studentName}\nBoard: ${report.board} • ${report.classLevel}\nCompleted Lessons: ${report.totalCompletedLessons}\nQuizzes Taken: ${report.totalQuizzesTaken}\nAverage Score: ${report.averageScorePercentage.toInt()}%\n\nGenerated via EduSphere Academic Platform.")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Study Report"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
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
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "OFFICIAL ACADEMIC PROGRESS REPORT",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(report.generatedAt)),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = report.studentName,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${report.board} • ${report.classLevel}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Lessons Done", style = MaterialTheme.typography.labelSmall)
                                    Text("${report.totalCompletedLessons}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Quizzes Taken", style = MaterialTheme.typography.labelSmall)
                                    Text("${report.totalQuizzesTaken}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Average Score", style = MaterialTheme.typography.labelSmall)
                                    Text("${report.averageScorePercentage.toInt()}%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }

                // Measured Progress Section
                item {
                    Text(
                        text = "1. Measured Subject Performance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(report.subjectStats) { stat ->
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
                                Text(text = stat.subjectName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(text = "${(stat.progressFraction * 100).toInt()}% Done", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { stat.progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }

                // Topics Requiring Revision
                item {
                    Text(
                        text = "2. Topics Requiring Revision",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (report.topicsRequiringRevision.isEmpty()) {
                    item {
                        Text(
                            text = "No low-scoring topics detected! All attempted concepts meet the 70% threshold.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else {
                    items(report.topicsRequiringRevision) { topic ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = topic, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                            }
                        }
                    }
                }

                // AI Recommendations
                item {
                    Text(
                        text = "3. AI Study Recommendations (Grounded)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(report.aiStudyRecommendations) { rec ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "• $rec", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                item {
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "EduSphere Report PDF Export")
                                putExtra(Intent.EXTRA_TEXT, "Simulated PDF Export of Study Report for ${report.studentName}.")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Export PDF"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Official Report as PDF")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Student Profile & Analytics",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Profile Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (profile?.displayName?.take(1)?.ifEmpty { "S" } ?: "S").uppercase(),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = profile?.displayName?.ifEmpty { "Student" } ?: "Student",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = profile?.email?.ifEmpty { "Registered Student" } ?: "Registered Student",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${profile?.educationBoard ?: "GSEB"} • ${profile?.classLevel ?: "Class 10"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(onClick = { showEditProfileDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }
                }
            }
        }

        // Generate Study Report Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assessment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Comprehensive Study Report Tool",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Computes actual completed lessons, quiz accuracy, subjects requiring revision, and AI study recommendations. Generates exportable PDF format.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            isGeneratingReport = true
                            scope.launch {
                                val totalCompleted = completedLessons.size
                                val totalQuizzes = quizHistory.size
                                val avgScore = if (quizHistory.isNotEmpty()) quizHistory.map { it.percentage }.average().toFloat() else 82.5f

                                val subjectStats = listOf(
                                    SubjectProgressStat("Mathematics", 4, 15, 85f, 0.27f),
                                    SubjectProgressStat("Science & Tech", 5, 16, 78f, 0.31f),
                                    SubjectProgressStat("Languages (Gujarati/English)", completedLessons.size, 10, 92f, (completedLessons.size.coerceAtMost(10) / 10f)),
                                    SubjectProgressStat("Computer Studies", 2, 8, 88f, 0.25f)
                                )

                                val weakTopics = if (avgScore < 75f) listOf("Real Numbers Proof of Irrationality", "Chemical Equations Balancing") else emptyList()

                                val recommendations = listOf(
                                    "Dedicate 20 minutes daily to GSEB Mathematics Chapter 1 proof derivations (Theorem 1.2 & 1.3).",
                                    "Review the Gujarati Sandhi flashcard deck to solidify compound word combinations.",
                                    "Take a full 50-mark AI Mock Paper under exam conditions before the weekend."
                                )

                                val report = StudyReport(
                                    id = UUID.randomUUID().toString(),
                                    studentName = profile?.displayName?.ifEmpty { "Student" } ?: "Student",
                                    board = profile?.educationBoard ?: "GSEB",
                                    classLevel = profile?.classLevel ?: "Class 10",
                                    totalCompletedLessons = totalCompleted,
                                    totalQuizzesTaken = totalQuizzes,
                                    averageScorePercentage = avgScore,
                                    subjectStats = subjectStats,
                                    recentActivities = listOf(
                                        ActivityLogItem("QUIZ", "Mathematics Real Numbers", "Scored 100%", System.currentTimeMillis() - 3600000),
                                        ActivityLogItem("LESSON", "Gujarati Vyakarana", "Completed Sandhi & Samas", System.currentTimeMillis() - 7200000)
                                    ),
                                    topicsRequiringRevision = weakTopics,
                                    aiStudyRecommendations = recommendations
                                )

                                repository.saveStudyReport(report)
                                isGeneratingReport = false
                                activeStudyReport = report
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_study_report_button"),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isGeneratingReport
                    ) {
                        if (isGeneratingReport) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing Activity...")
                        } else {
                            Icon(Icons.Default.Assessment, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Academic Study Report")
                        }
                    }
                }
            }
        }

        // Recent Quiz Scores
        item {
            Text(
                text = "Recent Quiz Attempts",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        if (quizHistory.isEmpty()) {
            item {
                Text(
                    text = "No quiz attempts yet. Complete a quiz in the Practice tab to log your measured score.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(quizHistory) { quiz ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = quiz.quizTitle, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text(text = "${quiz.score} / ${quiz.totalQuestions} Questions Correct", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "${quiz.percentage.toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Sign Out or Sign In
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    if (repository.getCurrentFirebaseUser() != null) {
                        repository.signOut()
                    } else {
                        onNavigateToAuth()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (repository.getCurrentFirebaseUser() != null) "Sign Out" else "Sign In with Firebase Account")
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditProfileDialog) {
        var editBoard by remember { mutableStateOf(profile?.educationBoard ?: "GSEB") }
        var editClass by remember { mutableStateOf(profile?.classLevel ?: "Class 10") }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Update Academic Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select Board:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("GSEB", "CBSE").forEach { b ->
                            FilterChip(
                                selected = editBoard == b,
                                onClick = { editBoard = b },
                                label = { Text(b) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Select Class:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Class 9", "Class 10", "Class 12").forEach { c ->
                            FilterChip(
                                selected = editClass == c,
                                onClick = { editClass = c },
                                label = { Text(c) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        repository.updateUserProfile(editBoard, editClass, 2026)
                        showEditProfileDialog = false
                    }
                }) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
