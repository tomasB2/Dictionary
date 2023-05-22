package com.example.demo

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class MeaningsControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private class WordsInput(val lang: String, val word: String)

    @Test
    fun `test getting word meaning in en without providing user`() {
        val expectedResp = "{\"wordInfo\":{\"lang\":\"en\",\"word\":\"horse\",\"phonetic\":\"/hɔɹs/\",\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"Any of several animals related to Equus ferus caballus.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Equipment with legs.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Type of equipment.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"A mass of earthy matter, or rock of the same character as the wall rock, occurring in the course of a vein, as of coal or ore; hence, to take horse (said of a vein) is to divide into branches for a distance.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"The sedative, antidepressant, and anxiolytic drug morphine, chiefly when used illicitly.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"An informal variant of basketball in which players match shots made by their opponent(s), each miss adding a letter to the word \\\"horse\\\", with 5 misses spelling the whole word and eliminating a player, until only the winner is left. Also HORSE, H-O-R-S-E or H.O.R.S.E. (see Variations of basketball#H-O-R-S-E).\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) A translation or other illegitimate aid in study or examination.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) horseplay; tomfoolery\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]}]}]},\"placedInUserHistory\":false,\"reason\":\"User not provided\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/words",
            WordsInput("en", "horse"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test getting word meaning in pt without providing user`() {
        val expectedResp = "{\"wordInfo\":{\"lang\":\"pt\",\"word\":\"cavalo\",\"phonetic\":null,\"meanings\":[{\"partOfSpeech\":null,\"definitions\":[{\"definition\":\"Quadrúpede doméstico, solípede.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade convencional, em Mechânica, equivalente à força necessaria para elevar 75 kilogrammas a 1 metro de altura em um segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Banco de tanoaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Ramo ou tronco, em que se enxerta.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Cancro sifilítico.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Peça de xadrez.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de uma carta de jogo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade de um corpo de cavalaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Tenaz de fogão.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de alguns peixes.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo vapor_, medida dinâmica, igual ao trabalho de 75 quilogrâmetros por segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo hora_, medida dinâmica, igual ao trabalho de um cavalo vapor numa hora.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Libra esterlina, cavalinho.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]}]}]},\"placedInUserHistory\":false,\"reason\":\"User not provided\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/words",
            WordsInput("pt", "cavalo"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test getting not available word meaning in en`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/words",
            WordsInput("en", "cavalo"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val body = response.body.toString()
        Assertions.assertThat(body.contains("\"status\":404,\"error\":\"Word not found\",\"path\":\"/api/words\""))
        Assertions.assertThat(body.contains("timestamp"))
    }

    @Test
    fun `test getting not available word meaning in pt`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/words",
            WordsInput("pt", "horse"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val body = response.body.toString()
        Assertions.assertThat(body.contains("\"status\":404,\"error\":\"Word not found\",\"path\":\"/api/words\""))
        Assertions.assertThat(body.contains("timestamp"))
    }

    @Test
    fun `test user trying to get word with invalid characters`() {
        val response = testRestTemplate.postForEntity(
            "/api/words",
            WordsInput("en", "cavalo"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val body = response.body.toString()
        Assertions.assertThat(body.contains("\"status\":404,\"error\":\"Word not found\",\"path\":\"/api/words\""))
        Assertions.assertThat(body.contains("timestamp"))
    }

    private class UserInput(val name: String, val email: String, val password: String)

    private class LoginInput(val name: String, val email: String, val password: String)

    @Test
    fun `test user login and getting word meaning with userToken`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing", "Testing@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        val expectedResp = "{\"wordInfo\":{\"lang\":\"en\",\"word\":\"horse\",\"phonetic\":\"/hɔɹs/\",\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"Any of several animals related to Equus ferus caballus.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Equipment with legs.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Type of equipment.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"A mass of earthy matter, or rock of the same character as the wall rock, occurring in the course of a vein, as of coal or ore; hence, to take horse (said of a vein) is to divide into branches for a distance.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"The sedative, antidepressant, and anxiolytic drug morphine, chiefly when used illicitly.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"An informal variant of basketball in which players match shots made by their opponent(s), each miss adding a letter to the word \\\"horse\\\", with 5 misses spelling the whole word and eliminating a player, until only the winner is left. Also HORSE, H-O-R-S-E or H.O.R.S.E. (see Variations of basketball#H-O-R-S-E).\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) A translation or other illegitimate aid in study or examination.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) horseplay; tomfoolery\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]}]}]},\"placedInUserHistory\":true,\"reason\":null}"
        response = testRestTemplate.postForEntity(
            "/api/words",
            HttpEntity(WordsInput("en", "horse"), headers),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test user getting word meaning with userToken wrong`() {
        val headers = HttpHeaders()
        headers.add("Authorization", "abc")
        val expectedResp = "{\"wordInfo\":{\"lang\":\"en\",\"word\":\"horse\",\"phonetic\":\"/hɔɹs/\",\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"Any of several animals related to Equus ferus caballus.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Equipment with legs.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Type of equipment.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"A mass of earthy matter, or rock of the same character as the wall rock, occurring in the course of a vein, as of coal or ore; hence, to take horse (said of a vein) is to divide into branches for a distance.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"The sedative, antidepressant, and anxiolytic drug morphine, chiefly when used illicitly.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"An informal variant of basketball in which players match shots made by their opponent(s), each miss adding a letter to the word \\\"horse\\\", with 5 misses spelling the whole word and eliminating a player, until only the winner is left. Also HORSE, H-O-R-S-E or H.O.R.S.E. (see Variations of basketball#H-O-R-S-E).\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) A translation or other illegitimate aid in study or examination.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) horseplay; tomfoolery\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]}]}]},\"placedInUserHistory\":false,\"reason\":\"User not found\"}"
        val response = testRestTemplate.postForEntity(
            "/api/words",
            HttpEntity(WordsInput("en", "horse"), headers),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }
}
