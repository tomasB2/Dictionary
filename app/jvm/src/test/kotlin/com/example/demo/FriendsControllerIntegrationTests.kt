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
class FriendsControllerIntegrationTests {
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private class UserInput(val name: String, val email: String, val password: String)

    private class LoginInput(val name: String?, val email: String?, val password: String)

    @Test
    fun `test create users`() {
        var expectedResp = "{\"name\":\"user1\",\"email\":\"user1@testing\"}"
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("user1", "user1@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)

        expectedResp = "{\"name\":\"user2\",\"email\":\"user2@testing\"}"
        response = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test get friends(receive empty)`() {
        val response = testRestTemplate.getForEntity(
            "/api/users/friends/user2",
            String::class.java,
        )
        val resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("[]"))
    }

    @Test
    fun `test get friends from non existent`() {
        val response = testRestTemplate.getForEntity(
            "/api/users/friends/user5",
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("\"error\":\"User not Found\""))
        Assertions.assertThat(resp.contains("\"status\": 404"))
        Assertions.assertThat(resp.contains("\"path\": \"/api/users/friends/name\""))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test login and add non existant friend`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/user5",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        resp = response.body.toString()
        Assertions.assertThat(resp.contains("\"error\":\"User not Found\""))
        Assertions.assertThat(resp.contains("\"status\": 400"))
        Assertions.assertThat(resp.contains("\"path\": \"/api/users/friends/name\""))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test login and add friend`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/user1",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `tests accept friend request`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user1", "user1@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/accept/user2",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        println(resp)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `test get friends(receive 1)`() {
        var response = testRestTemplate.getForEntity(
            "/api/users/friends/user2",
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("[{\"name\":\"user1\",\"email\":\"user1@testing\"}]"))
        response = testRestTemplate.getForEntity(
            "/api/users/friends/user1",
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        resp = response.body.toString()
        Assertions.assertThat(resp.contains("[{\"name\":\"user2\",\"email\":\"user2@testing\"}]"))
    }

    @Test
    fun`test login and remove non existent friend`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/user5",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(resp.contains("\"error\":\"User not Found\""))
        Assertions.assertThat(resp.contains("\"status\": 400"))
        Assertions.assertThat(resp.contains("\"path\": \"/api/users/friends/name\""))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test login and remove friend`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/user1",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `test login and add get friends(receive empty again)`() {
        val response = testRestTemplate.getForEntity(
            "/api/users/friends/user2",
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("[]"))
    }

    @Test
    fun `test login and add friend(2)`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user2", "user2@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/user1",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        println(resp)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `tests decline friend request`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user1", "user1@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/decline/user2",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        println(resp)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(resp.contains("true"))
    }

    @Test
    fun `tests decline friend request with no friend request`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("user1", "user1@testing", "passworD1"),
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        Assertions.assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/friends/decline/user5",
            org.springframework.http.HttpMethod.PUT,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        println(resp)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(resp.contains("\"error\":\"Failed requirement.\",\"cause\":\"USER_BAD_REQUEST\"}"))
        Assertions.assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test login and add get friends(receive empty(3))`() {
        val response = testRestTemplate.getForEntity(
            "/api/users/friends/user2",
            String::class.java,
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val resp = response.body.toString()
        Assertions.assertThat(resp.contains("[]"))
    }
}
