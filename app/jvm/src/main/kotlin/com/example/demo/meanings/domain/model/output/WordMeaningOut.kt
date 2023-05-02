package com.example.demo.meanings.domain.model.output

import com.example.demo.meanings.domain.model.WordInfo

data class WordMeaningOut(
    val wordInfo: WordInfo?,
    val placedInUserHistory: Boolean,
    val reason: String?,
)
