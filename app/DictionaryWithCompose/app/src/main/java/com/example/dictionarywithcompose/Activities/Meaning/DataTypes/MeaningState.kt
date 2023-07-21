package com.example.dictionarywithcompose.Activities.Meaning.DataTypes // ktlint-disable package-name

data class MeaningState(
    val displayText: String = "",
    val uiState: UiState = UiState.SEARCH_STATE,
    val lang: String = "en",
)
