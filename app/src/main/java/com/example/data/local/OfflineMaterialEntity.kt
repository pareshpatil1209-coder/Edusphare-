package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_materials")
data class OfflineMaterialEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val board: String,
    val classLevel: String,
    val subject: String,
    val chapter: String,
    val resourceType: String,
    val language: String,
    val publisher: String,
    val fullTextContent: String,
    val downloadedAt: Long = System.currentTimeMillis()
)
