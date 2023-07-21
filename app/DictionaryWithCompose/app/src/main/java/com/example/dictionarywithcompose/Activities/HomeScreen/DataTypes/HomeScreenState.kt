package com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes

import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent

data class HomeState(
    val lastSearches: MeaningContent = MeaningContent(),
    val isfirst: Boolean = true,
    val islast: Boolean = false,
)
