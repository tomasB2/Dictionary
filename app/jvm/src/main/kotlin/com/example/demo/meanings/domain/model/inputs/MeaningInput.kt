package com.example.demo.meanings.domain.model.inputs

data class MeaningInput(
    val lang: String,
    val word: String,
    val userToken: String?,
)
