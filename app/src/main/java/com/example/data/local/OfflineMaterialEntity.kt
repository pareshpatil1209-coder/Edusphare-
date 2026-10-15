package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_materials")
data class OfflineMaterialEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subject: String,
    val classLevel: String,
    val educationBoard: String,
    val chapter: String,
    val type: String, // "notes", "paper", "formula", "textbook"
    val content: String,
    val summary: String,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = true,
    val downloadedAt: Long = System.currentTimeMillis()
)
