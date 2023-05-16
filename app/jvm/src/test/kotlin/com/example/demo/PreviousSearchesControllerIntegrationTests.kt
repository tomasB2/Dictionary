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
class PreviousSearchesControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private class UserInput(val name: String, val email: String, val password: String)

    private class LoginInput(val name: String?, val email: String?, val password: String)

    private class WordsInput(val lang: String, val word: String)

    @Test
    fun `test create users`() {
        val expectedResp = "{\"name\":\"user3\",\"email\":\"user3@testing\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test get searches return empty`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("[]"))
    }

    @Test
    fun `test user login and getting word meaning with userToken`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
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
    fun `test get searches return 1`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("[{\"lang\":\"en\",\"word\":\"horse\",\"phonetic\":\"/h??s/\",\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"Any of several animals related to Equus ferus caballus.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Equipment with legs.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Type of equipment.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"A mass of earthy matter, or rock of the same character as the wall rock, occurring in the course of a vein, as of coal or ore; hence, to take horse (said of a vein) is to divide into branches for a distance.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"The sedative, antidepressant, and anxiolytic drug morphine, chiefly when used illicitly.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"An informal variant of basketball in which players match shots made by their opponent(s), each miss adding a letter to the word \\\"horse\\\", with 5 misses spelling the whole word and eliminating a player, until only the winner is left. Also HORSE, H-O-R-S-E or H.O.R.S.E. (see Variations of basketball#H-O-R-S-E).\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) A translation or other illegitimate aid in study or examination.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) horseplay; tomfoolery\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]}]}]}]")) // change
    }

    @Test
    fun `test user login and getting word meaning with userToken(2)`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        val expectedResp = "{\"wordInfo\":{\"lang\":\"pt\",\"word\":\"cavalo\",\"phonetic\":null,\"meanings\":[{\"partOfSpeech\":null,\"definitions\":[{\"definition\":\"Quadrúpede doméstico, solípede.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade convencional, em Mechânica, equivalente à força necessaria para elevar 75 kilogrammas a 1 metro de altura em um segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Banco de tanoaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Ramo ou tronco, em que se enxerta.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Cancro sifilítico.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Peça de xadrez.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de uma carta de jogo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade de um corpo de cavalaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Tenaz de fogão.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de alguns peixes.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo vapor_, medida dinâmica, igual ao trabalho de 75 quilogrâmetros por segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo hora_, medida dinâmica, igual ao trabalho de um cavalo vapor numa hora.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Libra esterlina, cavalinho.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]}]}]}"
        response = testRestTemplate.postForEntity(
            "/api/words",
            HttpEntity(WordsInput("pt", "cavalo"), headers),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body != null && response.body!!.contains(expectedResp))
    }

    @Test
    fun `test get searches return 2`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("{\"lang\":\"en\",\"word\":\"horse\",\"phonetic\":\"/h??s/\",\"meanings\":[{\"partOfSpeech\":\"noun\",\"definitions\":[{\"definition\":\"Any of several animals related to Equus ferus caballus.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Equipment with legs.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Type of equipment.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"A mass of earthy matter, or rock of the same character as the wall rock, occurring in the course of a vein, as of coal or ore; hence, to take horse (said of a vein) is to divide into branches for a distance.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"The sedative, antidepressant, and anxiolytic drug morphine, chiefly when used illicitly.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"An informal variant of basketball in which players match shots made by their opponent(s), each miss adding a letter to the word \\\"horse\\\", with 5 misses spelling the whole word and eliminating a player, until only the winner is left. Also HORSE, H-O-R-S-E or H.O.R.S.E. (see Variations of basketball#H-O-R-S-E).\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) A translation or other illegitimate aid in study or examination.\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"(among students) horseplay; tomfoolery\",\"example\":\"\",\"synonyms\":[],\"antonyms\":[]}]}]}"))
        Assertions.assertThat(resp.contains("{\"wordInfo\":{\"lang\":\"pt\",\"word\":\"cavalo\",\"phonetic\":null,\"meanings\":[{\"partOfSpeech\":null,\"definitions\":[{\"definition\":\"Quadrúpede doméstico, solípede.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade convencional, em Mechânica, equivalente à força necessaria para elevar 75 kilogrammas a 1 metro de altura em um segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Banco de tanoaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Ramo ou tronco, em que se enxerta.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Cancro sifilítico.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Peça de xadrez.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de uma carta de jogo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade de um corpo de cavalaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Tenaz de fogão.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de alguns peixes.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo vapor_, medida dinâmica, igual ao trabalho de 75 quilogrâmetros por segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo hora_, medida dinâmica, igual ao trabalho de um cavalo vapor numa hora.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Libra esterlina, cavalinho.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]}]}]}"))
    }

    @Test
    fun `delete one search`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches/cavalo",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `delete one search with non existant word`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches/abc",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `delete one search with bad token`() {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer abc")
        val response = testRestTemplate.exchange(
            "/api/searches/cavalo",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(null, headers),
            String::class.java,
        )
        val resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(resp.contains(",\"error\":\"Failed requirement.\",\"cause\":\"USER_BAD_REQUEST\""))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test get searches return 1(2)`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        val expectedResp = "{\"wordInfo\":{\"lang\":\"pt\",\"word\":\"cavalo\",\"phonetic\":null,\"meanings\":[{\"partOfSpeech\":null,\"definitions\":[{\"definition\":\"Quadrúpede doméstico, solípede.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade convencional, em Mechânica, equivalente à força necessaria para elevar 75 kilogrammas a 1 metro de altura em um segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Banco de tanoaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Ramo ou tronco, em que se enxerta.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Cancro sifilítico.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Peça de xadrez.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de uma carta de jogo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Unidade de um corpo de cavalaria.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Tenaz de fogão.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Nome de alguns peixes.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo vapor_, medida dinâmica, igual ao trabalho de 75 quilogrâmetros por segundo.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"_Cavalo hora_, medida dinâmica, igual ao trabalho de um cavalo vapor numa hora.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]},{\"definition\":\"Libra esterlina, cavalinho.\",\"example\":null,\"synonyms\":[],\"antonyms\":[]}]}]}"
        response = testRestTemplate.postForEntity(
            "/api/words",
            HttpEntity(WordsInput("pt", "cavalo"), headers),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body != null && response.body!!.contains(expectedResp))
    }

    @Test
    fun `delete searches`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.DELETE,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `delete searches with bad token`() {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer abc")
        val response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.DELETE,
            HttpEntity(null, headers),
            String::class.java,
        )
        val resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(resp.contains(",\"error\":\"Failed requirement.\",\"cause\":\"USER_BAD_REQUEST\""))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test get searches return empty(2)`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user3", "user3@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/searches",
            org.springframework.http.HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("[]"))
    }
}
