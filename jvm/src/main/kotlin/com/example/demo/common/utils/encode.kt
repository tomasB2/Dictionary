package com.example.demo.common.utils // ktlint-disable filename

import java.security.MessageDigest
import java.util.*

fun encodeString(text: String): String {
    return hash(text)
}

private fun hash(input: String): String {
    val messageDigest = MessageDigest.getInstance("SHA256")
    return Base64.getUrlEncoder().encodeToString(
        messageDigest.digest(
            Charsets.UTF_8.encode(input).array(),
        ),
    )
}
