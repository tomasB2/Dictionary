package com.example.demo.transaltions.service // ktlint-disable package-name

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.transaltions.domain.Language
import com.example.demo.transaltions.domain.TranslationDAO
import com.example.demo.transaltions.domain.Translator
import com.example.demo.transaltions.domain.languageOf
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("TranslationService")
class TranslationService : TranslationServiceInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun translateText(text: String, from: String?, to: String): Response<TranslationDAO?> {
        logger.info("translateText for: {}, {}, {}", text, from, to)
        return try {
            val fromLang = if (from == null) Language.AUTO else languageOf(from) ?: Language.AUTO
            val toLang = languageOf(to)
                ?: return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
            if (toLang == Language.AUTO) {
                return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
            }
            val translation = Translator.translate(text, toLang, fromLang)
            Response(res = translation, e = null)
        } catch (e: Exception) {
            logger.error("translateText for: {}, {}, {}, e: {}", text, from, to, e.message)
            throw e
        }
    }
}
