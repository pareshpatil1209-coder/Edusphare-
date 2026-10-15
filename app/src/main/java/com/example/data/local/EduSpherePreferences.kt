package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "edusphere_settings")

class EduSpherePreferences(private val context: Context) {

    companion object {
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode") // "SYSTEM", "LIGHT", "DARK"
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val KEY_OFFLINE_AUTO_SYNC = booleanPreferencesKey("offline_auto_sync")
        val KEY_TTS_SPEED = stringPreferencesKey("tts_speed") // "0.75", "1.0", "1.25"
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_THEME_MODE] ?: "SYSTEM"
    }

    val notificationsEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_NOTIFICATIONS_ENABLED] ?: true
    }

    val offlineAutoSyncFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_OFFLINE_AUTO_SYNC] ?: true
    }

    val ttsSpeedFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_TTS_SPEED] ?: "1.0"
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setOfflineAutoSync(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_OFFLINE_AUTO_SYNC] = enabled
        }
    }

    suspend fun setTtsSpeed(speed: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TTS_SPEED] = speed
        }
    }
}
