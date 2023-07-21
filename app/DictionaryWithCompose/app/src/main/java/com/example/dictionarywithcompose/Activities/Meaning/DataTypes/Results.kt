package com.example.dictionarywithcompose.Activities.Meaning.DataTypes

data class Results(
    val meanings: List<MeaningContent>,
    val currentIndex: Int,
    val isFirst: Boolean = true,
    val isLast: Boolean = true,
)
