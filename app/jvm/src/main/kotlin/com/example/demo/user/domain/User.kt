package com.example.demo.user.domain

data class User(
    val name: String,
    val verify: String,
    val email: String,
) {
    init {
        require(name.isNotEmpty()) { "Name must not be empty" }
        require(verify.isNotEmpty()) { "Password must not be empty" }
        require(email.isNotEmpty()) { "Email must not be empty" }
    }
}
