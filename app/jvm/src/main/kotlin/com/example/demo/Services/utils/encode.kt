package com.example.demo.Services.utils

import java.security.MessageDigest
import java.util.*

// ktlint-disable filename


fun encodeString(text: String): String {
    return hash(text)
}

private fun hash(input: String): String {
    val messageDigest = MessageDigest.getInstance("SHA256")
    return Base64.getUrlEncoder().encodeToString(
        messageDigest.digest(
            Charsets.UTF_8.encode(input).array()
        )
    )
}