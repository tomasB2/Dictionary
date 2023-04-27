package com.example.demo.Services // ktlint-disable package-name

import com.example.demo.domain.meanings.model.WordInfo
import domain.Response

interface PreviousSearchServiceInterface {
    fun getPreviousSearches(userName: String): Response<List<WordInfo>>
    fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo): Response<Boolean>
    fun deletePreviousSearch(userName: String, searchKey: String): Response<Boolean>
    fun deleteAllPreviousSearches(userName: String): Response<Boolean>
}
