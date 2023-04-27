package com.example.dictionarywithcompose.Activities.SpeechRecognition // ktlint-disable package-name

import androidx.lifecycle.ViewModel
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    val uiState = MutableStateFlow(MainScreenState(""))

    fun changeTextValue(text: String) {
        uiState.value = uiState.value.copy(
            text = text,
        )
    }
}
