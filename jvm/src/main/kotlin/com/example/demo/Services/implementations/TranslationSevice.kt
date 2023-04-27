package com.example.demo.Services.implementations // ktlint-disable package-name

import com.example.demo.Services.TranslationServiceInterface
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import domain.translator.Language
import domain.translator.TranslationDAO
import domain.translator.Translator
import domain.translator.languageOf
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("TranslationService")
class TranslationService : TranslationServiceInterface {
    override fun translateText(text: String, from: String?, to: String): Response<TranslationDAO?> {
        val fromLang = if (from == null) Language.AUTO else languageOf(from) ?: Language.AUTO
        val toLang = languageOf(to)
            ?: return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
        if (toLang == Language.AUTO) {
            return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
        }
        val translation = Translator.translate(text, toLang, fromLang)
        return Response(res = translation, e = null)
    }
}
