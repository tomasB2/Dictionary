package com.example.dictionarywithcompose.Activities.Translation // ktlint-disable package-name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionarywithcompose.Activities.Common.Speech.ButtonForSpeech
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState
import com.example.dictionarywithcompose.Activities.Meaning.IconDropDown
import com.example.dictionarywithcompose.Activities.Meaning.InputText
import com.example.dictionarywithcompose.Activities.Meaning.SearchPageBody
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationContents
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationState
import com.example.dictionarywithcompose.R

@Composable
fun TranslationComposable(
    displayWord: TranslationState,
    uiState: TranslationContents,
    updateDisplayWord: (word: String) -> Unit,
    handleUiState: (uiState: UiState) -> Unit,
    attemptToSearchForNewWord: (word: String, langTo: String) -> Unit,
    updateLanguage: (String) -> Unit,
    CameraView: @Composable () -> Unit,
) {
    if (displayWord.uiState == UiState.TAKING_PHOTO) {
        CameraView()
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                SearchBar(
                    string = displayWord.text,
                    language = displayWord.langTo,
                    updateDisplayWord = updateDisplayWord,
                    handleUiState = handleUiState,
                    changeLanguage = {
                        updateLanguage(it)
                    },
                    searchForWord = attemptToSearchForNewWord,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                ButtonToTranslate(attemptToSearchForNewWord = attemptToSearchForNewWord, displayWord = displayWord)
            }

            DisplayTranslatedText(displayWord = uiState)
        }
    }
}

@Composable
fun SearchBar(
    string: String,
    language: String,
    updateDisplayWord: (word: String) -> Unit,
    handleUiState: (uiState: UiState) -> Unit,
    changeLanguage: (lang: String) -> Unit,
    searchForWord: (word: String, lang: String) -> Unit,
) {
    InputText(viewModelState = string, language= language,  updateDisplayWord = updateDisplayWord,  searchForWord ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ButtonForSpeech(changeText = {
                updateDisplayWord(it)
            })
            IconButton(onClick = {
                handleUiState(UiState.TAKING_PHOTO)
            }) {
                Icon(
                    imageVector = Icons.Sharp.Camera,
                    contentDescription = "Camera",
                )
            }
            IconDropDown(
                availableLanguages = mapOf<String, Int>(
                    "pt" to R.drawable.portugal_flag,
                    "en" to R.drawable.united_kingdom,
                ),
                changeLanguage = changeLanguage,
            )
        }
    }
}

@Composable
fun ButtonToTranslate(
    attemptToSearchForNewWord: (word: String, langTo: String) -> Unit,
    displayWord: TranslationState,
) {
    Button(
        onClick = {
            attemptToSearchForNewWord(displayWord.text, displayWord.langTo)
        },
        modifier = Modifier
            .padding(16.dp),
    ) {
        Text(
            text = "Translate",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@Composable
fun DisplayTranslatedText(displayWord: TranslationContents) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchPageBody(modifier = Modifier) {
            Text(text = "Translated Text:", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp), textAlign = TextAlign.Center)
            Text(
                text = displayWord.text,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}
