package com.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class PreviousSearchesControllerIntegrationTests {

    // TODO("
    //  Add more tests for the endpoints and error handling, including the following:
    //  - Test the error handling for the endpoints
    //  - Test invalid input for the endpoints
    //  ")

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `test`() {
        TODO("Test class with User Controller Integration Test with example")
    }
}
