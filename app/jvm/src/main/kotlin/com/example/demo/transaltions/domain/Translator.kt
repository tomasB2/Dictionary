package com.example.demo.transaltions.domain

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import java.io.FileInputStream

object Translator {

    private val credentials: GoogleCredentials = GoogleCredentials.fromStream(
        FileInputStream("translatorKey.json"),
    )

    private val service = TranslateOptions.newBuilder()
        .setCredentials(credentials)
        .build()
        .service

    fun translate(
        text: String,
        target: Language,
        source: Language = Language.AUTO,
    ): TranslationDAO {
        val translation: Translation = service.translate(text, Translate.TranslateOption.targetLanguage(target.code))
        return TranslationDAO(
            sourceLanguage = languageOf(translation.sourceLanguage) ?: Language.AUTO,
            sourceText = text,
            targetLanguage = target,
            targetText = translation.translatedText,
        )
    }
}
