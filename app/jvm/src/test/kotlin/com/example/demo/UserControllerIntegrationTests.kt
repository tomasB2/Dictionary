package com.example.demo

import org.assertj.core.api.Assertions.assertThat
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
class UserControllerIntegrationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    private class UserInput(val name: String, val email: String, val password: String)

    private class LoginInput(val name: String?, val email: String?, val password: String)

    @Test
    fun `test create user`() {
        val expectedResp = "{\"name\":\"Testing\",\"email\":\"Testing@testing\"}"
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("Testing", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test create user fail by bad name`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("name", "Testing23@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val resp = response.body.toString()
        assertThat(resp.contains("\"message\":\"Name must be contain 5-16 characters and no special characters\""))
        assertThat(resp.contains("\"cause\":\"USER_BAD_REQUEST\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test create user fail by duplicate name`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("Testing", "Testing23@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Username already taken\""))
        assertThat(resp.contains("\"status\":400"))
        assertThat(resp.contains("\"timestamp\":"))
        assertThat(resp.contains("\"path\":\"/api/users/create\""))
    }

    @Test
    fun `test create user fail by bad password`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("Testing2", "Testing23@testing", "passworD"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Password must be contain 8-16 characters, at least one uppercase letter, one lowercase letter ane one number\""))
        assertThat(resp.contains("\"timestamp\":"))
        assertThat(resp.contains("\"cause\":\"USER_BAD_REQUEST\""))
    }

    @Test
    fun `test create user fail by duplicate email`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/create",
            UserInput("Testing2", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Email already taken\""))
        assertThat(resp.contains("\"status\":400"))
        assertThat(resp.contains("\"timestamp\":"))
        assertThat(resp.contains("\"path\":\"/api/users/create\""))
    }

    @Test
    fun `test get user by id, fail`() {
        val userId = "abc"
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/users/$userId", String::class.java, userId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\": \"User not found\""))
        assertThat(resp.contains("\"status\": 404"))
        assertThat(resp.contains("\"path\": \"/api/users/name\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test get user by id`() {
        val userId = "Testing"
        val expectedResp = "{\"name\":\"Testing\",\"email\":\"Testing@testing\"}"
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/users/$userId", String::class.java, userId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test get users, ask for 1`() {
        val userId = "Testing"
        val limit = 1
        val offset = 0
        val expectedResp = "[{\"name\":\"Testing\",\"email\":\"Testing@testing\"}]"
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/users?limit=$limit&offset=$offset", String::class.java, userId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test get users, ask for 0`() {
        val userId = "Test"
        val limit = 0
        val offset = 0
        val expectedResp = "[]"
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/users?limit=$limit&offset=$offset", String::class.java, userId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResp)
    }

    @Test
    fun `test user login and fail by password`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing", "Testing@testing", "passworD"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Invalid username or password\""))
        assertThat(resp.contains("\"status\": 401"))
        assertThat(resp.contains("\"path\": \"/api/users/login\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test user login and fail by username`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing1", null, "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Invalid username or password\""))
        assertThat(resp.contains("\"status\": 401"))
        assertThat(resp.contains("\"path\": \"/api/users/login\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test user login and fail by email`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput(null, "Testing@testssing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Invalid username or password\""))
        assertThat(resp.contains("\"status\": 401"))
        assertThat(resp.contains("\"path\": \"/api/users/login\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test user login and fail by not providing user`() {
        val response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput(null, null, "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        val resp = response.body.toString()
        assertThat(resp.contains("\"error\":\"Invalid username or password\""))
        assertThat(resp.contains("\"status\": 401"))
        assertThat(resp.contains("\"path\": \"/api/users/login\""))
        assertThat(resp.contains("\"timestamp\":"))
    }

    @Test
    fun `test user login and logout`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/logout",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.contains("true"))
    }

    @Test
    fun `test user check token`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/token",
            org.springframework.http.HttpMethod.GET,
            HttpEntity(null, headers),
            String::class.java,
        )
        resp = response.body.toString()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.contains("true"))
    }

    @Test
    fun `test login user and edit name`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testing", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        assertThat(resp.contains("{\"name\":\"Testing\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/edit",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(UserInput("Testin", "Testing@testing", "passworD1"), headers),
            String::class.java,
        )
        resp = response.body.toString()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.contains("{\"name\":\"Testin\",\"email\":\"Testing@testing\"}"))
    }

    @Test
    fun `test login user and edit email`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testin", "Testing@testing", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/edit",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(UserInput("Testin", "Testing@testin", "passworD1"), headers),
            String::class.java,
        )
        resp = response.body.toString()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.contains("{\"name\":\"Testin\",\"email\":\"Testing@testin\"}"))
    }

    @Test
    fun `test login user and edit password`() {
        var response: ResponseEntity<String> = testRestTemplate.postForEntity(
            "/api/users/login",
            LoginInput("Testin", "Testing@testin", "passworD1"),
            String::class.java,
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        var resp = response.body.toString()
        assertThat(resp.contains("{\"name\":\"Testin\",\"token\":"))
        val split = resp.split("token\":\"")
        val token = split[1].split("\"}")[0]
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        response = testRestTemplate.exchange(
            "/api/users/edit",
            org.springframework.http.HttpMethod.POST,
            HttpEntity(UserInput("Testin", "Testing@testin", "passworD12"), headers),
            String::class.java,
        )
        resp = response.body.toString()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.contains("{\"name\":\"Testin\",\"email\":\"Testing@testin\"}"))
    }
}
