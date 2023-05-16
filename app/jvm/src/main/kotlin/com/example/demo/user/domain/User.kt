package com.example.demo.user.domain

import com.example.demo.common.utils.isValidEmail
import com.example.demo.common.utils.isValidName

data class User(
    val id: Int,
    val name: String,
    val verify: String,
    val email: String,
) {
    init {
        require(name.isValidName()) { "Name must be contain 5-16 characters and no special characters" }
        require(verify.isNotEmpty()) { "Verify must not be empty" }
        require(email.isValidEmail()) { "Email must be contain 5-30 characters and must be in email format" }
    }
}
