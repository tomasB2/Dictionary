package com.example.demo.Services.implementations // ktlint-disable package-name

import com.example.demo.Services.PreviousSearchServiceInterface
import com.example.demo.domain.meanings.model.WordInfo
import com.example.demo.repository.implementations.TransactionManagerImp
import domain.Response
import org.springframework.stereotype.Service

@Suppress("unused")
@Service("PreviousSearchServiceImp")
class PreviousSearchServiceImp : PreviousSearchServiceInterface {
    override fun getPreviousSearches(userName: String): Response<List<WordInfo>> {
        return TransactionManagerImp.run { transaction ->
            transaction.previousSearchesRepository.getPreviousSearches(userName)
        }
    }

    override fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            transaction.previousSearchesRepository.addPreviousSearch(userName, searchKey, search)
            Response(res = true, e = null)
        }
    }

    override fun deletePreviousSearch(userName: String, searchKey: String): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            transaction.previousSearchesRepository.deletePreviousSearch(userName, searchKey)
            Response(res = true, e = null)
        }
    }

    override fun deleteAllPreviousSearches(userName: String): Response<Boolean> {
        return TransactionManagerImp.run { transaction ->
            transaction.previousSearchesRepository.deleteAllPreviousSearches(userName)
            Response(res = true, e = null)
        }
    }
}
