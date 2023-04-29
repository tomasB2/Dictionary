package com.example.demo.meanings.domain

import com.example.demo.meanings.domain.model.WordInfo

interface WordResolver {

    val baseUrl: String

    fun getMeaning(word: String): WordInfo?
}
