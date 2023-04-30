package com.example.demo

import org.assertj.core.api.Assertions.assertThat
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
class UserControllerIntegrationTests {

    // VERY IMPORTANT: REMOVE HTTPS CONFIG FOR LOCAL TESTING(FOR NOW)
    // THE testRestTemplate IS LIKE A MINI SERVER THAT CAN BE USED TO TEST THE ENDPOINTS

    // TODO("
    //  Add more tests for the endpoints and error handling, including the following:
    //  - Test the error handling for the endpoints
    //  - Test invalid input for the endpoints
    //  ")

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `test get user by id`() {
        val userId = "Test"
        val expectedResp = "{\"name\":\"Test\",\"email\":\"Testing@testing\"}"
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/users/$userId", String::class.java, userId)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(expectedResp)
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
    fun `test get users, ask for 1`() {
        val userId = "Test"
        val limit = 1
        val offset = 0
        val expectedResp = "[{\"name\":\"Test\",\"email\":\"Testing@testing\"}]"
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
}
