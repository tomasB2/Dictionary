package com.example.demo.meanings

import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.meanings.domain.model.inputs.MeaningInput
import com.example.demo.meanings.service.MeaningServiceInterface
import com.example.demo.previousSearches.services.PreviousSearchServiceInterface
import com.example.demo.user.service.UserServiceInterface
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

    private val logger = org.slf4j.LoggerFactory.getLogger(MeaningController::class.java)

    @PostMapping(Uris.Word.GET_MEANING)
    fun findWord(
        @RequestBody data: MeaningInput,
    ): Any {
        logger.info("MeaningController.findWord: {}", data)
        val word = meaningServices.getMeaning(data.userToken, data.lang, data.word)
        return responseGenerator(word, Uris.Word.GET_MEANING) {
            word.res
        }
    }
}
