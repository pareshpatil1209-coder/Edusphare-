package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OfflineMaterialEntity::class], version = 1, exportSchema = false)
abstract class EduSphereDatabase : RoomDatabase() {

    abstract fun offlineMaterialDao(): OfflineMaterialDao

    companion object {
        @Volatile
        private var INSTANCE: EduSphereDatabase? = null

        fun getDatabase(context: Context): EduSphereDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduSphereDatabase::class.java,
                    "edusphere_database"
                ).fallbackToDestructiveMigration(dropAllTables = true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
