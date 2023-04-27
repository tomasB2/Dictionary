package com.example.demo.Services // ktlint-disable package-name

import domain.Response
import domain.translator.TranslationDAO

interface TranslationServiceInterface {
    /**
     * Function that transalates text from one language to another
     */
    fun translateText(text: String, from: String?, to: String): Response<TranslationDAO?>
}
