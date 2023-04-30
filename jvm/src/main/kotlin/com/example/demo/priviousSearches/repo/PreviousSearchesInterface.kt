package com.example.demo.priviousSearches.repo

import com.example.demo.common.domain.Response
import com.example.demo.meanings.domain.model.WordInfo

interface PreviousSearchesInterface {
    fun getPreviousSearches(userName: String): Response<List<WordInfo>>
    fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo)
    fun deletePreviousSearch(userName: String, searchKey: String)
    fun deleteAllPreviousSearches(userName: String)
}
