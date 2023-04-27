package com.example.demo.Services.implementations // ktlint-disable package-name

import com.example.demo.Services.MeaningServiceInterface
import com.example.demo.domain.meanings.model.WordInfo
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import domain.meanings.LanguageResolver
import domain.translator.languageOf
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("MeaningService")
class MeaningService : MeaningServiceInterface {
    override fun getMeaning(lang: String, word: String): Response<WordInfo?> {
        val langCheck = languageOf(lang)
            ?: return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
        val wordInfo = LanguageResolver.resolveLanguage(langCheck)?.getMeaning(word)
            ?: return Response(res = null, e = ErrorResponse(message = "Word not found", cause = ErrorCause.WORD_NOT_FOUND))
        return Response(res = wordInfo, e = null)
    }
}
