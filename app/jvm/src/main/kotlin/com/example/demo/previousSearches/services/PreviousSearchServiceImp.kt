package com.example.demo.previousSearches.services // ktlint-disable package-name

import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.utils.authenticate
import com.example.demo.meanings.domain.model.WordInfo
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("PreviousSearchServiceImp")
class PreviousSearchServiceImp : PreviousSearchServiceInterface {
    override fun wordExistsInUser(userName: String, searchKey: String): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            val userId =
                transaction.usersRepository.getUserByName(userName).res?.id ?: return@run Response(res = false, e = null)
            transaction.previousSearchesRepository.wordExistsInUser(userId, searchKey)
        }
    }

    override fun getPreviousSearch(searchKey: String): Response<WordInfo?> {
        return TransactionManagerImp.run { transaction ->
            transaction.previousSearchesRepository.getPreviousSearch(searchKey)
        }
    }

    override fun getPreviousSearches(token: String): Response<List<WordInfo?>?> {
        return TransactionManagerImp.run { transaction ->
            val user = authenticate(token, transaction)
            if (user.res == null) return@run Response(res = null, e = user.e)
            val userId = user.res.id
            transaction.previousSearchesRepository.getPreviousSearches(userId)
        }
    }

    override fun addPreviousSearch(token: String, searchKey: String, search: WordInfo): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            val user = authenticate(token, transaction)
            if (user.res == null) return@run Response(res = false, e = user.e)
            val userId = user.res.id
            transaction.previousSearchesRepository.addPreviousSearch(userId, searchKey, search)
            Response(res = true, e = null)
        }
    }

    override fun deletePreviousSearch(token: String, searchKey: String): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            val user = authenticate(token, transaction)
            if (user.res == null) return@run Response(res = false, e = user.e)
            val userId = user.res.id
            transaction.previousSearchesRepository.deletePreviousSearch(userId, searchKey)
            Response(res = true, e = null)
        }
    }

    override fun deleteAllPreviousSearches(token: String): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            val user = authenticate(token, transaction)
            if (user.res == null) return@run Response(res = false, e = user.e)
            val userId = user.res.id
            transaction.previousSearchesRepository.deleteAllPreviousSearches(userId)
            Response(res = true, e = null)
        }
    }
}
