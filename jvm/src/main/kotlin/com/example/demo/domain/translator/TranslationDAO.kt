package domain.translator

data class TranslationDAO(
    val sourceLanguage: Language,
    val sourceText: String,
    val targetLanguage: Language,
    val targetText: String,
)