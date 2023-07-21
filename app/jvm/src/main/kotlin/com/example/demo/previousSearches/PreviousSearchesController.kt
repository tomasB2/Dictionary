package com.example.demo.previousSearches

import com.example.demo.common.http.outputStructures.toBooleanOut
import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.previousSearches.services.PreviousSearchServiceInterface
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@Suppress("unused")
@RestController
class PreviousSearchesController(
    val previousSearchesInterface: PreviousSearchServiceInterface,
) {
    @GetMapping(Uris.PreviousSearches.GET_PREVIOUS_SEARCHES)
    fun getPreviousSearches(
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.getPreviousSearches(token)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res
        }
    }

    @PostMapping(Uris.PreviousSearches.DELETE_PREVIOUS_SEARCHES_BY_ID)
    fun delPreviousSearch(
        @RequestHeader("Authorization") token: String,
        @PathVariable word: String,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.deletePreviousSearch(token, searchKey = word)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res.toBooleanOut()
        }
    }

    @DeleteMapping(Uris.PreviousSearches.DELETE_PREVIOUS_SEARCHES_BY_USER)
    fun delPreviousSearches(
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<*> {
        val resp = previousSearchesInterface.deleteAllPreviousSearches(token)
        return responseGenerator(resp, Uris.PreviousSearches.GET_PREVIOUS_SEARCHES) {
            resp.res.toBooleanOut()
        }
    }
}
