package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineMaterialDao {

    @Query("SELECT * FROM offline_materials ORDER BY downloadedAt DESC")
    fun getAllOfflineMaterials(): Flow<List<OfflineMaterialEntity>>

    @Query("SELECT * FROM offline_materials WHERE subject = :subject ORDER BY downloadedAt DESC")
    fun getMaterialsBySubject(subject: String): Flow<List<OfflineMaterialEntity>>

    @Query("SELECT * FROM offline_materials WHERE isBookmarked = 1 ORDER BY downloadedAt DESC")
    fun getBookmarkedMaterials(): Flow<List<OfflineMaterialEntity>>

    @Query("SELECT * FROM offline_materials WHERE id = :id LIMIT 1")
    suspend fun getMaterialById(id: String): OfflineMaterialEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: OfflineMaterialEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<OfflineMaterialEntity>)

    @Query("UPDATE offline_materials SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmarkStatus(id: String, isBookmarked: Boolean)

    @Query("DELETE FROM offline_materials WHERE id = :id")
    suspend fun deleteMaterial(id: String)
}
