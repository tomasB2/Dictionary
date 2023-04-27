package com.example.demo.http

import com.example.demo.Services.MeaningServiceInterface
import com.example.demo.Services.PreviousSearchServiceInterface
import com.example.demo.Services.UserServiceInterface
import com.example.demo.http.inputStructures.MeaningInput
import com.example.demo.http.outputStructures.WordMeaningOut
import com.example.demo.http.utils.Uris
import com.example.demo.http.utils.responseGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class MeaningController(
    val meaningServices: MeaningServiceInterface,
    val previousSearchesInterface: PreviousSearchServiceInterface,
    val userServices: UserServiceInterface,
) {
    @PostMapping(Uris.Word.GET_MEANING)
    fun findWord(
        @RequestBody data: MeaningInput,
    ): Any {
        var placed = false
        var reason: String? = null
        val word = meaningServices.getMeaning(data.lang, data.word)
        if (data.userToken != null && word.res != null) {
            val user = userServices.getUserByToken(data.userToken)
            if (user.res != null) {
                previousSearchesInterface.addPreviousSearch(user.res.name, data.word, word.res)
                placed = true
            }
        } else {
            reason = "User not found"
        }
        return responseGenerator(
            word,
            Uris.Word.GET_MEANING,
        ) {
            WordMeaningOut(
                wordInfo = word.res,
                placedInUserHistory = placed,
                reason = reason,
            )
        }
    }
}
