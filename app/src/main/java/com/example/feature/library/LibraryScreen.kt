package com.example.feature.library

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.local.EduSphereDatabase
import com.example.data.local.OfflineMaterialEntity
import com.example.data.model.AuthorizedEducationalCatalog
import com.example.data.model.BookmarkItem
import com.example.data.model.ChapterItem
import com.example.data.model.EducationalResource
import com.example.data.model.EducationalSeedData
import com.example.data.repository.EduSphereRepository
import com.example.feature.ai.EduSphereTtsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    repository: EduSphereRepository,
    database: EduSphereDatabase,
    ttsManager: EduSphereTtsManager,
    onStartChapterQuiz: (ChapterItem) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Multi-dimensional filters
    var searchQuery by remember { mutableStateOf("") }
    var selectedBoard by remember { mutableStateOf("All") }
    var selectedClass by remember { mutableStateOf("All") }
    var selectedSubject by remember { mutableStateOf("All") }
    var selectedResourceType by remember { mutableStateOf("All") }
    var selectedLanguage by remember { mutableStateOf("All") }
    var showFilterPanel by remember { mutableStateOf(false) }

    // Active reading state
    var viewingResource by remember { mutableStateOf<EducationalResource?>(null) }
    var viewingChapter by remember { mutableStateOf<ChapterItem?>(null) }

    val bookmarks by repository.observeBookmarks().collectAsState(initial = emptyList())
    val offlineMaterials by database.offlineMaterialDao().getAllOfflineMaterials().collectAsState(initial = emptyList())
    val isSpeaking by ttsManager.isSpeaking.collectAsState()

    // Filter combined resources
    val filteredResources = remember(
        searchQuery, selectedBoard, selectedClass, selectedSubject, selectedResourceType, selectedLanguage
    ) {
        AuthorizedEducationalCatalog.resources.filter { res ->
            val matchBoard = selectedBoard == "All" || res.board.equals(selectedBoard, ignoreCase = true)
            val matchClass = selectedClass == "All" || res.classLevel.contains(selectedClass, ignoreCase = true)
            val matchSubject = selectedSubject == "All" || res.subject.contains(selectedSubject, ignoreCase = true)
            val matchType = selectedResourceType == "All" || res.resourceType.equals(selectedResourceType, ignoreCase = true)
            val matchLang = selectedLanguage == "All" || res.language.equals(selectedLanguage, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                    res.title.contains(searchQuery, ignoreCase = true) ||
                    res.subject.contains(searchQuery, ignoreCase = true) ||
                    res.chapter.contains(searchQuery, ignoreCase = true) ||
                    res.summary.contains(searchQuery, ignoreCase = true)
            matchBoard && matchClass && matchSubject && matchType && matchLang && matchSearch
        }
    }

    // Back handler for detail views
    BackHandler(enabled = viewingResource != null || viewingChapter != null) {
        ttsManager.stop()
        viewingResource = null
        viewingChapter = null
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (viewingResource != null) {
                val res = viewingResource!!
                val isBookmarked = bookmarks.any { it.referenceId == res.id }
                val isDownloaded = offlineMaterials.any { it.id == res.id }

                TopAppBar(
                    title = {
                        Text(
                            text = res.title,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            ttsManager.stop()
                            viewingResource = null
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        // Official link opener
                        IconButton(onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.officialUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Unable to open browser") }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Open Official Website")
                        }

                        // TTS Button
                        IconButton(onClick = {
                            if (isSpeaking) {
                                ttsManager.stop()
                            } else {
                                val readText = "${res.title}. Published by ${res.source}. Summary: ${res.summary}. ${res.fullContent.take(600)}"
                                ttsManager.speak(readText)
                            }
                        }) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = "Read Aloud",
                                tint = if (isSpeaking) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Bookmark
                        IconButton(onClick = {
                            coroutineScope.launch {
                                if (isBookmarked) {
                                    val item = bookmarks.firstOrNull { it.referenceId == res.id }
                                    if (item != null) {
                                        repository.removeBookmark(item.id)
                                        snackbarHostState.showSnackbar("Removed from bookmarks")
                                    }
                                } else {
                                    val item = BookmarkItem(
                                        title = res.title,
                                        type = "resource",
                                        subject = res.subject,
                                        referenceId = res.id
                                    )
                                    val added = repository.addBookmark(item)
                                    if (added.isSuccess) {
                                        snackbarHostState.showSnackbar("Bookmarked successfully!")
                                    }
                                }
                            }
                        }) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Download Offline
                        IconButton(onClick = {
                            coroutineScope.launch {
                                val entity = OfflineMaterialEntity(
                                    id = res.id,
                                    title = res.title,
                                    subject = res.subject,
                                    classLevel = res.classLevel,
                                    educationBoard = res.board,
                                    chapter = res.chapter,
                                    type = res.resourceType.lowercase().replace(" ", "_"),
                                    content = res.fullContent,
                                    summary = res.summary,
                                    isBookmarked = isBookmarked,
                                    isDownloaded = true
                                )
                                database.offlineMaterialDao().insertMaterial(entity)
                                snackbarHostState.showSnackbar("Downloaded for offline reading! (Room DB)")
                            }
                        }) {
                            Icon(
                                imageVector = if (isDownloaded) Icons.Default.DownloadDone else Icons.Default.CloudDownload,
                                contentDescription = "Download Offline",
                                tint = if (isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            } else if (viewingChapter != null) {
                val current = viewingChapter!!
                val isBookmarked = bookmarks.any { it.referenceId == current.id }
                TopAppBar(
                    title = { Text(current.title, maxLines = 1) },
                    navigationIcon = {
                        IconButton(onClick = {
                            ttsManager.stop()
                            viewingChapter = null
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (isSpeaking) {
                                ttsManager.stop()
                            } else {
                                ttsManager.speak("${current.title}. ${current.summary}. ${current.keyFormulasOrNotes.joinToString(". ")}")
                            }
                        }) {
                            Icon(if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp, contentDescription = "TTS")
                        }
                        IconButton(onClick = {
                            coroutineScope.launch {
                                val entity = OfflineMaterialEntity(
                                    id = current.id,
                                    title = current.title,
                                    subject = current.subjectId,
                                    classLevel = "Class 10",
                                    educationBoard = "GSEB/CBSE",
                                    chapter = "Chapter ${current.number}",
                                    type = "study_notes",
                                    content = current.content,
                                    summary = current.summary,
                                    isBookmarked = isBookmarked,
                                    isDownloaded = true
                                )
                                database.offlineMaterialDao().insertMaterial(entity)
                                snackbarHostState.showSnackbar("Saved for offline reading!")
                            }
                        }) {
                            Icon(Icons.Default.CloudDownload, contentDescription = "Download Offline")
                        }
                    }
                )
            }
        }
    ) { padding ->
        if (viewingResource != null) {
            val res = viewingResource!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Metadata Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = res.board,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = res.classLevel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = res.resourceType,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = res.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Source Attribution Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Authorized Source: ${res.source}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Licensing: ${res.licensing} (${res.editionYear})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.officialUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    coroutineScope.launch { snackbarHostState.showSnackbar("Unable to open portal") }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Official Education Portal", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Executive Summary:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = res.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Complete Study Material & Notes:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = res.fullContent,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        } else if (viewingChapter != null) {
            val chapter = viewingChapter!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Chapter ${chapter.number} • ${chapter.subjectId.uppercase()} • ${chapter.readingTimeMinutes} mins read",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = chapter.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Formulas Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📌 Key Formulas & Exam Points:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        chapter.keyFormulasOrNotes.forEach { note ->
                            Text(text = "• $note", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = chapter.content,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onStartChapterQuiz(chapter) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Attempt Chapter Quiz (${chapter.sampleQuestions.size} Questions)")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            // Main Library Browser
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .testTag("library_screen")
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Educational Resource Library",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Authorized GSEB & CBSE textbooks, notes, and past papers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { showFilterPanel = !showFilterPanel },
                        modifier = Modifier.testTag("filter_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Toggle Filters",
                            tint = if (showFilterPanel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by topic, board, or subject...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("library_search_input")
                )

                // Expandable Filter Panel
                AnimatedVisibility(visible = showFilterPanel) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Education Board",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(listOf("All", "GSEB", "CBSE")) { b ->
                                    FilterChip(
                                        selected = selectedBoard == b,
                                        onClick = { selectedBoard = b },
                                        label = { Text(b, fontSize = 12.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Class & Stream",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(listOf("All", "Class 9", "Class 10", "Class 11 Science", "Class 12 Science")) { c ->
                                    FilterChip(
                                        selected = selectedClass == c,
                                        onClick = { selectedClass = c },
                                        label = { Text(c, fontSize = 12.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Resource Type",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(listOf("All", "E-Textbook", "Study Notes", "Previous Year Question Paper", "Workbook")) { t ->
                                    FilterChip(
                                        selected = selectedResourceType == t,
                                        onClick = { selectedResourceType = t },
                                        label = { Text(t, fontSize = 12.sp) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Subject Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedSubject == "All",
                            onClick = { selectedSubject = "All" },
                            label = { Text("All Subjects") }
                        )
                    }
                    items(listOf("Mathematics", "Science", "Physics", "Chemistry", "Social Science", "Gujarati")) { sub ->
                        FilterChip(
                            selected = selectedSubject == sub,
                            onClick = { selectedSubject = sub },
                            label = { Text(sub) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Showing ${filteredResources.size} authorized materials",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Resource Cards List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredResources) { res ->
                        val isBookmarked = bookmarks.any { it.referenceId == res.id }
                        val isDownloaded = offlineMaterials.any { it.id == res.id }

                        ElevatedCard(
                            onClick = { viewingResource = res },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("resource_card_${res.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Surface(
                                            color = if (res.board == "GSEB") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = res.board,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = if (res.board == "GSEB") MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = res.classLevel,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Surface(
                                            color = MaterialTheme.colorScheme.tertiaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = res.resourceType,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Row {
                                        if (isDownloaded) {
                                            Icon(
                                                Icons.Default.DownloadDone,
                                                contentDescription = "Downloaded Offline",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        if (isBookmarked) {
                                            Icon(
                                                Icons.Default.Bookmark,
                                                contentDescription = "Bookmarked",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = res.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = res.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🏛 ${res.source.take(28)}...",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = "Open Reader →",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
