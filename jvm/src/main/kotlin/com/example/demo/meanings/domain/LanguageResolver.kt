package com.example.demo.meanings.domain

import com.example.demo.meanings.domain.resolvers.EnglishWordResolver // ktlint-disable import-ordering
import com.example.demo.meanings.domain.resolvers.PortugueseWordResolver
// import meanings.resolvers.* // ktlint-disable no-wildcard-imports
import com.example.demo.transaltions.domain.Language

object LanguageResolver {

    private val map = mutableMapOf<Language, WordResolver>()

    init {
        map[Language.ENGLISH] = EnglishWordResolver()
        map[Language.PORTUGUESE] = PortugueseWordResolver()
        // map[Language.SPANISH] = SpanishWords()
        // map[Language.FRENCH] = FrenchWords()
        // map[Language.GERMAN] = GermanWords()
        // map[Language.ITALIAN] = ItalianWords()
    }

    fun resolveLanguage(language: Language): WordResolver? = map[language]
}
