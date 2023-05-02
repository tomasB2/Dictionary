package com.example.demo.transaltions

import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.transaltions.domain.inputStructures.TranslationInput
import com.example.demo.transaltions.service.TranslationServiceInterface
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class TranslationController(
    private val translationService: TranslationServiceInterface,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(Uris.Translation.GET_TRANSLATION)
    fun getTranslationHandler(
        @RequestBody data: TranslationInput,
    ): ResponseEntity<*> {
        logger.info("getTranslationHandler for: {}", data)
        val resp = translationService.translateText(data.text, data.from, data.to)
        return responseGenerator(resp, Uris.Translation.GET_TRANSLATION) {
            resp.res
        }
    }
}
