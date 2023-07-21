package com.example.dictionarywithcompose.Activities.Translation.DataTypes

import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState

data class TranslationState(
    val uiState: UiState = UiState.SEARCH_STATE,
    val text: String = "",
    val langFrom: String = "",
    val langTo: String = "",
)