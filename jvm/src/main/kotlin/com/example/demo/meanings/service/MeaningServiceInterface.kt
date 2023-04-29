package com.example.demo.meanings.service // ktlint-disable package-name

import com.example.demo.meanings.domain.model.output.WordMeaningOut
import com.example.demo.common.domain.Response

interface MeaningServiceInterface {

    fun getMeaning(userToken: String?, lang: String, word: String): Response<WordMeaningOut?>
}
