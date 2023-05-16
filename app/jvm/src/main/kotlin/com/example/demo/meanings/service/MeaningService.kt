package com.example.demo.meanings.service // ktlint-disable package-name

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.utils.isValidWord
import com.example.demo.meanings.domain.LanguageResolver
import com.example.demo.meanings.domain.model.WordInfo
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
            word.isValidWord()
            var placed = false
            var reason: String?
            return TransactionManagerImp.run { transaction ->
                val exists = transaction.previousSearchesRepository.getPreviousSearch(word).res
                return@run if (userToken == null) {
                    reason = "User not provided"
                    if (exists != null && exists.lang == lang) {
                        Response(res = WordMeaningOut(wordInfo = exists, placedInUserHistory = placed, reason = reason), e = null)
                    } else {
                        val wordInfo = getWordFromApi(lang, word)
                        if (wordInfo != null) {
                            Response(res = WordMeaningOut(wordInfo = wordInfo, placedInUserHistory = placed, reason = reason), e = null)
                        } else {
                            Response(res = null, e = ErrorResponse(error = "Word not found", cause = ErrorCause.WORD_NOT_FOUND))
                        }
                    }
                } else {
                    val token = userToken.replace("Bearer ", "")
                    val user = transaction.usersRepository.getUserByToken(token)
                    if (exists != null && exists.lang == lang) {
                        if (user.res != null) {
                            if (transaction.previousSearchesRepository.wordExistsInUser(user.res.id, word).res) {
                                reason = "Word already exists in user history"
                            } else {
                                transaction.previousSearchesRepository.addPreviousSearch(user.res.id, word, exists)
                                reason = null
                                placed = true
                            }
                        } else {
                            reason = "User not found"
                        }
                        Response(res = WordMeaningOut(wordInfo = exists, placedInUserHistory = placed, reason = reason), e = null)
                    } else {
                        val wordInfo = getWordFromApi(lang, word)
                        if (wordInfo != null) {
                            if (user.res != null) {
                                if (transaction.previousSearchesRepository.wordExistsInUser(user.res.id, word).res) {
                                    reason = "Word already exists in user history"
                                } else {
                                    transaction.previousSearchesRepository.addPreviousSearch(user.res.id, word, wordInfo)
                                    reason = null
                                    placed = true
                                }
                            } else {
                                reason = "User not found"
                            }
                            Response(res = WordMeaningOut(wordInfo = wordInfo, placedInUserHistory = placed, reason = reason), e = null)
                        } else {
                            Response(res = null, e = ErrorResponse(error = "Word not found", cause = ErrorCause.WORD_NOT_FOUND))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("getMeaning for: {}, {}, {}, e: {}", userToken, lang, word, e.message)
            throw e
        }
    }

    private fun getWordFromApi(lang: String, word: String): WordInfo? {
        val langCheck = languageOf(lang)
            ?: return null
        return LanguageResolver.resolveLanguage(langCheck)?.getMeaning(word)
    }
}
