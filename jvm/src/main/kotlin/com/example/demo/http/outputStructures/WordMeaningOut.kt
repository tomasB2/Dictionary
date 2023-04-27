package com.example.demo.http.outputStructures

import com.example.demo.domain.meanings.model.WordInfo

data class WordMeaningOut(
    val wordInfo: WordInfo?,
    val placedInUserHistory: Boolean,
    val reason: String?,
)
