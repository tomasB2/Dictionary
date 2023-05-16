package com.example.demo.user.domain.inputs // ktlint-disable filename

data class LoginInput(
    val name: String?,
    val email: String?,
    val password: String,
)
