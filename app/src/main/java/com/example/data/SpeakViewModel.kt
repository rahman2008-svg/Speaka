package com.example.data

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class SpeakViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val database = AppDatabase.getDatabase(application)
    private val repository = SpeakRepository(database.speakDao())

    // Database state flows
    val allHistory: StateFlow<List<SpeakItem>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteSnippets: StateFlow<List<SpeakItem>> = repository.favoriteItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // TTS engine instance
    private var tts: TextToSpeech? = null

    // UI state flows
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _languages = MutableStateFlow<List<AppLanguage>>(VoiceProfiles.languages)
    val languages: StateFlow<List<AppLanguage>> = _languages.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<AppLanguage?>(null)
    val selectedLanguage: StateFlow<AppLanguage?> = _selectedLanguage.asStateFlow()

    private val _voices = MutableStateFlow<List<AppVoice>>(emptyList())
    val voices: StateFlow<List<AppVoice>> = _voices.asStateFlow()

    private val _selectedVoice = MutableStateFlow<AppVoice?>(null)
    val selectedVoice: StateFlow<AppVoice?> = _selectedVoice.asStateFlow()

    private val _pitch = MutableStateFlow(1.0f)
    val pitch: StateFlow<Float> = _pitch.asStateFlow()

    private val _rate = MutableStateFlow(1.0f)
    val rate: StateFlow<Float> = _rate.asStateFlow()

    private val _textInput = MutableStateFlow("")
    val textInput: StateFlow<String> = _textInput.asStateFlow()

    private val _exportStatus = MutableStateFlow<String?>(null)
    val exportStatus: StateFlow<String?> = _exportStatus.asStateFlow()

    init {
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            setupTtsEngine()
        } else {
            Log.e("SpeakViewModel", "TTS Initialization failed!")
            _isTtsReady.value = false
        }
    }

    private fun setupTtsEngine() {
        val currentTts = tts ?: return

        // Set utterance progress listener
        currentTts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isPlaying.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isPlaying.value = false
                _exportStatus.value = null
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isPlaying.value = false
                _exportStatus.value = "Error occurred"
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isPlaying.value = false
                _exportStatus.value = "Error code: $errorCode"
            }
        })

        // Setup languages and select English US by default
        _languages.value = VoiceProfiles.languages
        val defaultLang = VoiceProfiles.languages.find { it.code == "en_US" } ?: VoiceProfiles.languages.first()
        setLanguage(defaultLang)

        _isTtsReady.value = true
    }

    fun setTextInput(text: String) {
        _textInput.value = text
    }

    fun setLanguage(appLanguage: AppLanguage) {
        _selectedLanguage.value = appLanguage
        try {
            tts?.language = appLanguage.locale
        } catch (e: Exception) {
            Log.e("SpeakViewModel", "Error setting language in TTS: ${e.message}")
        }

        // Filter voices for this custom language
        val filteredVoices = VoiceProfiles.voices.filter { it.languageCode == appLanguage.code }
        _voices.value = filteredVoices

        // Default to first voice of this language
        val defaultVoice = filteredVoices.firstOrNull()
        defaultVoice?.let { setVoice(it) }
    }

    fun setVoice(voice: AppVoice) {
        _selectedVoice.value = voice
    }

    fun setPitch(value: Float) {
        _pitch.value = value
    }

    fun setRate(value: Float) {
        _rate.value = value
    }

    fun speak() {
        val text = _textInput.value
        if (text.isBlank() || !_isTtsReady.value) return

        val currentVoice = _selectedVoice.value
        val basePitch = currentVoice?.basePitch ?: 1.0f
        val baseRate = currentVoice?.baseRate ?: 1.0f

        val finalPitch = basePitch * _pitch.value
        val finalRate = baseRate * _rate.value

        try {
            tts?.setPitch(finalPitch)
            tts?.setSpeechRate(finalRate)

            // Attempt to select native voice matching gender/locale if possible
            currentVoice?.let { appVoice ->
                val systemVoices = try { tts?.voices?.toList() } catch (e: Exception) { null } ?: emptyList()
                val systemLocale = VoiceProfiles.languages.find { it.code == appVoice.languageCode }?.locale ?: Locale.US
                
                val matchingVoice = systemVoices.filter { it.locale.language == systemLocale.language }
                    .find { sv ->
                        val nameLower = sv.name.lowercase()
                        val isMaleSystem = nameLower.contains("male") || nameLower.contains("masculine")
                        val isFemaleSystem = nameLower.contains("female") || nameLower.contains("feminine")
                        if (appVoice.gender == VoiceGender.MALE) {
                            isMaleSystem || (!isFemaleSystem && sv.name.contains("guy", ignoreCase = true))
                        } else {
                            isFemaleSystem || (!isMaleSystem && sv.name.contains("girl", ignoreCase = true))
                        }
                    } ?: systemVoices.find { it.locale.language == systemLocale.language }

                matchingVoice?.let { tts?.voice = it }
            }
        } catch (e: Exception) {
            Log.e("SpeakViewModel", "Error applying voice settings to TTS engine: ${e.message}")
        }

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "speak_utterance")
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "speak_utterance")
        _isPlaying.value = true

        // Insert into history as a background task
        saveSpeechToDatabase(text)
    }

    fun stop() {
        tts?.stop()
        _isPlaying.value = false
    }

    fun exportToAudioFile(context: Context, text: String, onComplete: (File?) -> Unit) {
        if (text.isBlank() || !_isTtsReady.value) {
            onComplete(null)
            return
        }

        _exportStatus.value = "Exporting..."

        val currentVoice = _selectedVoice.value
        val basePitch = currentVoice?.basePitch ?: 1.0f
        val baseRate = currentVoice?.baseRate ?: 1.0f

        val finalPitch = basePitch * _pitch.value
        val finalRate = baseRate * _rate.value

        try {
            tts?.setPitch(finalPitch)
            tts?.setSpeechRate(finalRate)

            currentVoice?.let { appVoice ->
                val systemVoices = try { tts?.voices?.toList() } catch (e: Exception) { null } ?: emptyList()
                val systemLocale = VoiceProfiles.languages.find { it.code == appVoice.languageCode }?.locale ?: Locale.US
                
                val matchingVoice = systemVoices.filter { it.locale.language == systemLocale.language }
                    .find { sv ->
                        val nameLower = sv.name.lowercase()
                        val isMaleSystem = nameLower.contains("male") || nameLower.contains("masculine")
                        val isFemaleSystem = nameLower.contains("female") || nameLower.contains("feminine")
                        if (appVoice.gender == VoiceGender.MALE) {
                            isMaleSystem || (!isFemaleSystem && sv.name.contains("guy", ignoreCase = true))
                        } else {
                            isFemaleSystem || (!isMaleSystem && sv.name.contains("girl", ignoreCase = true))
                        }
                    } ?: systemVoices.find { it.locale.language == systemLocale.language }

                matchingVoice?.let { tts?.voice = it }
            }
        } catch (e: Exception) {
            Log.e("SpeakViewModel", "Error setting settings for export: ${e.message}")
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) 
                    ?: context.cacheDir
                val outputFile = File(outputDir, "Speakify_${System.currentTimeMillis()}.wav")

                val params = Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "export_utterance")
                }

                // Call synthesize to file
                val result = tts?.synthesizeToFile(text, params, outputFile, "export_utterance")

                if (result == TextToSpeech.SUCCESS) {
                    withContext(Dispatchers.Main) {
                        _exportStatus.value = "Exported successfully!"
                        onComplete(outputFile)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _exportStatus.value = "Export failed"
                        onComplete(null)
                    }
                }
            } catch (e: Exception) {
                Log.e("SpeakViewModel", "Export error: ${e.message}")
                withContext(Dispatchers.Main) {
                    _exportStatus.value = "Export failed: ${e.message}"
                    onComplete(null)
                }
            }
        }
    }

    // Database Actions
    fun saveSpeechToDatabase(text: String, title: String = "") {
        if (text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val langCode = _selectedLanguage.value?.code ?: "en"
            val langName = _selectedLanguage.value?.displayName ?: "English"
            val item = SpeakItem(
                text = text,
                title = title,
                languageCode = langCode,
                languageName = langName,
                pitch = _pitch.value,
                rate = _rate.value,
                isFavorite = false
            )
            repository.insertItem(item)
        }
    }

    fun toggleFavorite(id: Int, currentFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(id, !currentFavorite)
        }
    }

    fun saveSnippetDirectly(text: String, title: String) {
        if (text.isBlank() || title.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val langCode = _selectedLanguage.value?.code ?: "en"
            val langName = _selectedLanguage.value?.displayName ?: "English"
            val item = SpeakItem(
                text = text,
                title = title,
                languageCode = langCode,
                languageName = langName,
                pitch = _pitch.value,
                rate = _rate.value,
                isFavorite = true
            )
            repository.insertItem(item)
        }
    }

    fun deleteItem(item: SpeakItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
        }
    }

    fun deleteItemById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItemById(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistoryOnly()
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }
}
