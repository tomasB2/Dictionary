package repository

import com.example.demo.domain.meanings.model.WordInfo
import domain.Response

interface PreviousSearchesInterface {
    fun getPreviousSearches(userName: String): Response<List<WordInfo>>
    fun addPreviousSearch(userName: String, searchKey: String, search: WordInfo)
    fun deletePreviousSearch(userName: String, searchKey: String)
    fun deleteAllPreviousSearches(userName: String)
}
