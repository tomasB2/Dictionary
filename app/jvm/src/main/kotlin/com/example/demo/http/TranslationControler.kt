package com.example.demo.http

import com.example.demo.Services.TranslationServiceInterface
import com.example.demo.http.inputStructures.MeaningInput
import com.example.demo.http.inputStructures.TranslationInput
import com.example.demo.http.utils.Uris
import com.example.demo.http.utils.responseGenerator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class TranslationControler(
    private val translationService: TranslationServiceInterface,
) {
    @PostMapping(Uris.Translation.GET_TRANSLATION)
    fun getTranslationHandler(
        @RequestBody data: TranslationInput,
    ): ResponseEntity<*> {
        val resp = translationService.translateText(data.text, data.from, data.to)
        return responseGenerator(resp, Uris.Translation.GET_TRANSLATION) {
            resp.res
        }
    }
}
