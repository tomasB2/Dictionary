package com.example.dictionarywithcompose.Activities.Translation // ktlint-disable package-name

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.CameraEvent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationContents
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationState
import com.example.dictionarywithcompose.Activities.Translation.Services.TranslationInput
import com.example.dictionarywithcompose.Activities.Translation.Services.TranslationRequestImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class TranslationViewModel : ViewModel() {
    val uiState = MutableStateFlow(TranslationState())
    val translatedContent = MutableStateFlow(TranslationContents())

    fun updateText(text: String) {
        uiState.value = uiState.value.copy(
            text = text,
        )
    }

    fun updateLangFrom(langFrom: String) {
        uiState.value = uiState.value.copy(
            langFrom = langFrom,
        )
    }

    fun updateLangTo(langTo: String) {
        uiState.value = uiState.value.copy(
            langTo = langTo,
        )
    }

    fun updateUiState(newUiState: UiState) {
        uiState.value = uiState.value.copy(
            uiState = newUiState,
        )
    }

    fun handleCameraEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.PictureTaken -> {
                updateText(event.text)
                updateUiState(UiState.SEARCH_STATE)
            }
            is CameraEvent.ChangeToPictureScreen -> {
                updateUiState(UiState.TAKING_PHOTO)
            }
            is CameraEvent.Error -> {
                updateText(event.text)
                updateUiState(UiState.SEARCH_STATE)
            }
        }
    }

    fun updateTranslation() {
        viewModelScope.launch {
            Log.v("TranslationViewModel", uiState.value.text)
            val map = TranslationRequestImpl.getTranslation(
                client = OkHttpClient(),
                TranslationInput(
                    "pt",
                    "en",
                    uiState.value.text,
                ),
            )
            Log.v("TranslationViewModel", map.toString())
            if (map.isNotEmpty()) {
                translatedContent.value = translatedContent.value.copy(text = map["target"] ?: "", langTo = map["target_language"] ?: "")
            }
        }
    }

    fun updateLanguage(s: String) {
        uiState.value = uiState.value.copy(
            langTo = s,
        )
        Log.v("TranslationViewModel", uiState.value.langTo)
    }
}
