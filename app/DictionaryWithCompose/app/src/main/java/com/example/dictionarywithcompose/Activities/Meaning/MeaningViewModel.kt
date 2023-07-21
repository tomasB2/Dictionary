package com.example.dictionarywithcompose.Activities.Meaning // ktlint-disable package-name

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.ClickEvent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.CameraEvent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningState
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.Results
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState
import com.example.dictionarywithcompose.Activities.Meaning.Services.MeaningImpl
import com.example.dictionarywithcompose.R
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class MeaningViewModel : ViewModel() {
    val meaningState = MutableStateFlow(MeaningState())
    val uiState = MutableStateFlow(
        MeaningContent(
            "Search",
            "to go or look through (a place, area, etc.) carefully in order to find something missing or lost",
            "They searched the woods for the missing child. I searched the desk for the letter.",
        ),
    )
    val listOfMeaning = MutableStateFlow(
        Results(
            meanings = mutableListOf(),
            currentIndex = 0,
        ),
    )
    val LANGUAGES_AVAILABLE = mutableMapOf<String, Int>(
        "pt" to R.drawable.portugal_flag,
        "en" to R.drawable.united_kingdom,
    ) // explorar a hipotese de alterar para um enum

    fun updateDisplayWord(word: String) {
        meaningState.value = meaningState.value.copy(displayText = word)
    }

    fun updateText(content: String?, uiState: UiState) {
        if (content != null) {
            meaningState.value = meaningState.value.copy(
                displayText = content,
                uiState = uiState,
            )
            return
        }
        meaningState.value = meaningState.value.copy(uiState = uiState)
    }

    fun handleCameraResult(event: CameraEvent) { // Existe de momento o problema de passar imensas funções para fazer cada coisa pequenina
        // Ficaria resolvido se houvesse um handleEvent e os eventos nao fossem apenas de camera. Assim Podiam ser definidas as funções de updateText Localmente e evitar
        // acomular tantos parametros, visto que está a ficar pouco extensivel o codigo
        when (event) {
            is CameraEvent.PictureTaken -> {
                updateText(
                    event.text,
                    UiState.SEARCH_STATE,
                )
            }
            is CameraEvent.ChangeToPictureScreen -> {
                updateText(
                    null,
                    UiState.TAKING_PHOTO,
                )
            }
            is CameraEvent.Error -> {
                updateText(
                    event.text,
                    UiState.SEARCH_STATE,
                )
            }
        }
    }

    fun handleUiState(newState: UiState) {
        meaningState.value = meaningState.value.copy(uiState = newState)
    }
    fun changeSearchedWord(
        word: String,
        meaning: String,
        example: String,
    ) {
        uiState.value = uiState.value.copy(
            text = word,
            meaning = meaning,
            example = example,
        )
    }

    fun attemptToSearchForNewWord(text: String, lang: String, context: Context) {
        viewModelScope.launch {
            if (text.isEmpty() || text == uiState.value.text || text.contains(". , + , * , ? , ^ , \$ , ( , ) , [ , ] , { , } , | , \\")) {
                return@launch
            }
            val okHttpClient = OkHttpClient()
            val list = MeaningImpl.getWord(okHttpClient, text, lang)
            if (list.isEmpty()) {
                Log.v("Meaning", "No meaning found")
                uiState.value = uiState.value.copy(
                    text = text,
                    meaning = "No meaning found",
                    example = "No example found",
                )
                return@launch
            }
            changeSearchedWord(
                word = text,
                meaning = list[0].meaning,
                example = list[0].example,
            )
            val dbHelper = MyDatabaseHelper(context)
            dbHelper.insertString(MeaningContent(text, list[0].meaning, list[0].example))
            Log.v("idk", dbHelper.getLastString().text)
            changeListOfMeaning(list)
            resetindexes()
        }
    }

    fun changeLanguage(s: String) {
        meaningState.value = meaningState.value.copy(lang = s)
    }
    fun changeIndex(click: ClickEvent) {
        when (click) {
            is ClickEvent.Next -> {
                val newIndex = listOfMeaning.value.currentIndex + 1
                listOfMeaning.value = listOfMeaning.value.copy(
                    currentIndex = newIndex,
                    isFirst = false,
                    isLast = newIndex == listOfMeaning.value.meanings.size - 1,
                )
                uiState.value = uiState.value.copy(
                    text = listOfMeaning.value.meanings[newIndex].text,
                    meaning = listOfMeaning.value.meanings[newIndex].meaning,
                    example = listOfMeaning.value.meanings[newIndex].example,
                )
            }
            is ClickEvent.Previous -> {
                val newIndex = listOfMeaning.value.currentIndex - 1
                listOfMeaning.value = listOfMeaning.value.copy(
                    currentIndex = newIndex,
                    isFirst = newIndex == 0,
                    isLast = false,
                )
                uiState.value = uiState.value.copy(
                    text = listOfMeaning.value.meanings[newIndex].text,
                    meaning = listOfMeaning.value.meanings[newIndex].meaning,
                    example = listOfMeaning.value.meanings[newIndex].example,
                )
            }
        }
    }
    fun changeListOfMeaning(list: List<MeaningContent>) {
        listOfMeaning.value = listOfMeaning.value.copy(
            meanings = list,
        )
    }
    fun resetindexes() {
        listOfMeaning.value = listOfMeaning.value.copy(
            currentIndex = 0,
            isFirst = true,
            isLast = false,
        )
    }
}
