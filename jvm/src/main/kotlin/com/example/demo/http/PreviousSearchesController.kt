package com.example.demo.http

import com.example.demo.Services.PreviousSearchServiceInterface
import com.example.demo.http.utils.Uris
import com.example.demo.http.utils.responseGenerator
import domain.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class PreviousSearchesController(
    val previousSearchesInterface: PreviousSearchServiceInterface,
) {
    @GetMapping(Uris.PreviousSearches.GET_PREVIOUS_SEARCHES)
    fun getPreviousSearches(
        @RequestBody user: User,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.getPreviousSearches(userName = user.name)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res
        }
    }

    @DeleteMapping(Uris.PreviousSearches.DELETE_PREVIOUS_SEARCHES_BY_ID)
    fun delPreviousSearch(
        @RequestBody user: User,
        @PathVariable word: String,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.deletePreviousSearch(userName = user.name, searchKey = word)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res
        }
    }

    @DeleteMapping(Uris.PreviousSearches.DELETE_PREVIOUS_SEARCHES_BY_USER)
    fun delPreviousSearches(
        @RequestBody user: User,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.deleteAllPreviousSearches(userName = user.name)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res
        }
    }
}
