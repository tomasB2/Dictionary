package domain.meanings

import domain.meanings.resolvers.EnglishWordResolver // ktlint-disable import-ordering
import domain.meanings.resolvers.PortugueseWordResolver
// import meanings.resolvers.* // ktlint-disable no-wildcard-imports
import domain.translator.Language

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
