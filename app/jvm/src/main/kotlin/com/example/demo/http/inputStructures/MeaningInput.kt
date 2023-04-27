package com.example.demo.http.inputStructures

data class MeaningInput(
    val lang: String,
    val word: String,
    val userToken: String?,
)
