package com.example.demo.user.domain

import com.example.demo.common.utils.isValidName

data class LogInInfo(val name: String, val token: String) {
    init {
        require(name.isValidName()) { "Name must be contain 5-16 characters and no special characters" }
        require(token.isNotEmpty()) { "Token must not be empty" }
    }
}
