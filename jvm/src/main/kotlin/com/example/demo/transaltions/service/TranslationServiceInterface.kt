package com.example.demo.transaltions.service // ktlint-disable package-name

import com.example.demo.transaltions.domain.TranslationDAO
import com.example.demo.common.domain.Response

interface TranslationServiceInterface {
    /**
     * Function that transalates text from one language to another
     */
    fun translateText(text: String, from: String?, to: String): Response<TranslationDAO?>
}
