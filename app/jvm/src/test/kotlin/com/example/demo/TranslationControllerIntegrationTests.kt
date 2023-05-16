package com.example.demo

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TranslationControllerIntegrationTests {

    @Autowired lateinit var testRestTemplate: TestRestTemplate

    private data class TranslationInput(
        val from: String?,
        val to: String,
        val text: String,
    )

    @Test
    fun `test getting translation from a know language`() {
        val expectedResp = "{\"sourceLanguage\":\"ENGLISH\",\"sourceText\":\"hello\",\"targetLanguage\":\"PORTUGUESE\",\"targetText\":\"olá\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/translation",
            TranslationInput("en", "pt", "hello"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test getting translation from an unknown language`() {
        val expectedResp = "{\"sourceLanguage\":\"ENGLISH\",\"sourceText\":\"hello\",\"targetLanguage\":\"PORTUGUESE\",\"targetText\":\"olá\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/translation",
            TranslationInput(null, "pt", "hello"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test fail getting translation a bad input text`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/translation",
            TranslationInput("en", "pt", "hello5\""),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val body = response.body.toString()
        Assertions.assertThat(body.contains("\"error\":\"Text must be contain only lowercase letters\",\"cause\":\"USER_BAD_REQUEST\""))
        Assertions.assertThat(body.contains("timestamp"))
    }

    @Test
    fun `test auto resolve getting translation a bad input language`() {
        val expectedResp = "{\"sourceLanguage\":\"ENGLISH\",\"sourceText\":\"hello\",\"targetLanguage\":\"PORTUGUESE\",\"targetText\":\"olá\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/translation",
            TranslationInput("rr", "pt", "hello"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test fail getting translation a bad output language`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/translation",
            TranslationInput("en", "rr", "hello"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val body = response.body.toString()
        Assertions.assertThat(body.contains("\"status\":400,\"error\":\"Invalid language\",\"path\":\"/api/translation\""))
        Assertions.assertThat(body.contains("timestamp"))
    }
}
