package com.example.demo.meanings.service // ktlint-disable package-name

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.meanings.domain.LanguageResolver
import com.example.demo.meanings.domain.model.output.WordMeaningOut
import com.example.demo.transaltions.domain.languageOf
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("MeaningService")
class MeaningService : MeaningServiceInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getMeaning(userToken: String?, lang: String, word: String): Response<WordMeaningOut?> {
        logger.info("getMeaning for: {}, {}, {}", userToken, lang, word)
        try {
            val langCheck = languageOf(lang)
                ?: return Response(res = null, e = ErrorResponse(message = "Invalid language", cause = ErrorCause.INVALID_LANGUAGE))
            val wordInfo = LanguageResolver.resolveLanguage(langCheck)?.getMeaning(word)
                ?: return Response(res = null, e = ErrorResponse(message = "Word not found", cause = ErrorCause.WORD_NOT_FOUND))
            var placed = false
            var reason: String? = null
            TransactionManagerImp.run { transaction ->
                if (userToken == null) return@run
                val user = transaction.usersRepository.getUserByToken(userToken)
                if (user.res != null) {
                    transaction.previousSearchesRepository.addPreviousSearch(user.res.name, word, wordInfo)
                    placed = true
                } else {
                    reason = "User not found"
                }
            }
            return Response(res = WordMeaningOut(wordInfo = wordInfo, placedInUserHistory = placed, reason = reason), e = null)
        } catch (e: Exception) {
            logger.error("getMeaning for: {}, {}, {}, e: {}", userToken, lang, word, e.message)
            throw e
        }
    }
}
