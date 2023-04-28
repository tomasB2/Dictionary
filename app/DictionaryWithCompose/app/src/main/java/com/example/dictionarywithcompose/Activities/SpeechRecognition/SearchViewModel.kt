package com.example.dictionarywithcompose.Activities.SpeechRecognition // ktlint-disable package-name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.MainScreenState
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.WordOnScreen
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val DisplayedWord = MutableStateFlow(WordOnScreen("Search", "to go or look through (a place, area, etc.) carefully in order to find something missing or lost", "They searched the woods for the missing child. I searched the desk for the letter."))

    val uiState = MutableStateFlow(MainScreenState(""))

    fun changeTextValue(text: String) {
        uiState.value = uiState.value.copy(
            text = text,
        )
    }
    fun searchForNewWord(text: String, db: MyDatabaseHelper) {
        val word = db.getAllStrings().find { it.word == text }
        if (word != null) {
            DisplayedWord.value = DisplayedWord.value.copy(
                word = word.word,
                meaning = word.meaning,
                example = word.example,
            )
        } else {
            attemptToSearchForNewWord(text)
        }
    }
    private fun attemptToSearchForNewWord(text: String) {
        viewModelScope.launch {
            // TODO("Make request to the api to get the word")
            // val word = api.getWord(text)
            // if (word != null) {
            //     DisplayedWord.value = word
            // } else {
            DisplayedWord.value = DisplayedWord.value.copy(
                word = text,
                meaning = "No meaning found",
                example = "No example found",
            )
        }
    }
}
