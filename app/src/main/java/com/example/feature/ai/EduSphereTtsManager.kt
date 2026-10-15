package com.example.feature.ai

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class EduSphereTtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking = _isSpeaking.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("EduSphereTTS", "US English not supported directly, falling back to default locale")
                tts?.setLanguage(Locale.getDefault())
            }
            isInitialized = true
        } else {
            Log.e("EduSphereTTS", "Initialization failed with status: $status")
        }
    }

    fun speak(text: String, speedMultiplier: Float = 1.0f) {
        if (!isInitialized) {
            Log.w("EduSphereTTS", "TTS not initialized yet")
            return
        }
        tts?.setSpeechRate(speedMultiplier)
        _isSpeaking.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "EduSphereSpeechUtterance")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        _isSpeaking.value = false
    }
}
