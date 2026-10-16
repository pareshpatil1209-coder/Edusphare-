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

    @Query("SELECT * FROM offline_materials WHERE id = :id LIMIT 1")
    suspend fun getOfflineMaterialById(id: String): OfflineMaterialEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM offline_materials WHERE id = :id)")
    fun isMaterialSaved(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: OfflineMaterialEntity)

    @Query("DELETE FROM offline_materials WHERE id = :id")
    suspend fun deleteMaterialById(id: String)
}
