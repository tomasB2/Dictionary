package com.example.demo.previousSearches.repo

import com.example.demo.common.domain.Response
import com.example.demo.meanings.domain.model.WordInfo

interface PreviousSearchesInterface {

    fun wordExistsInUser(userId: Int, searchKey: String): Response<Boolean>
    fun getPreviousSearch(searchKey: String): Response<WordInfo?>
    fun getPreviousSearches(userId: Int): Response<List<WordInfo?>?>
    fun addPreviousSearch(userId: Int, searchKey: String, search: WordInfo)
    fun deletePreviousSearch(userId: Int, searchKey: String)
    fun deleteAllPreviousSearches(userId: Int)
}
