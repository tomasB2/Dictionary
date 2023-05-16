package com.example.demo.previousSearches.services // ktlint-disable package-name

import com.example.demo.common.domain.Response
import com.example.demo.meanings.domain.model.WordInfo

interface PreviousSearchServiceInterface {
    fun wordExistsInUser(userName: String, searchKey: String): Response<Boolean>
    fun getPreviousSearch(searchKey: String): Response<WordInfo?>
    fun getPreviousSearches(token: String): Response<List<WordInfo?>?>
    fun addPreviousSearch(token: String, searchKey: String, search: WordInfo): Response<Boolean>
    fun deletePreviousSearch(token: String, searchKey: String): Response<Boolean>
    fun deleteAllPreviousSearches(token: String): Response<Boolean>
}
