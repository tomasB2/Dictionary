package com.example.demo.Services // ktlint-disable package-name

import com.example.demo.domain.meanings.model.WordInfo
import domain.Response

interface MeaningServiceInterface {

    fun getMeaning(lang: String, word: String): Response<WordInfo?>
}
