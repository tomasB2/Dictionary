package com.example.demo.priviousSearches.services // ktlint-disable package-name

import com.example.demo.common.domain.Response
import com.example.demo.meanings.domain.model.WordInfo

interface PreviousSearchServiceInterface {
    fun getPreviousSearches(userName: String): Response<List<WordInfo>>
    fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo): Response<Boolean>
    fun deletePreviousSearch(userName: String, searchKey: String): Response<Boolean>
    fun deleteAllPreviousSearches(userName: String): Response<Boolean>
}
