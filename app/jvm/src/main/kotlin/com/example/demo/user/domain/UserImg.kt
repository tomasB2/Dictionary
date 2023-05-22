package com.example.demo.user.domain

data class UserImg(
    val id: Int,
    val data: String, // bitmap
) {
    init {
        require(data.isNotEmpty()) { "Data must not be empty" }
    }
}
