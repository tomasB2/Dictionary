package com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes

import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent

data class DbState(
    val lastSearches: List<MeaningContent> = listOf(),
    val currentIndex: Int = 0,
)