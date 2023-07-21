package com.example.dictionarywithcompose.SqlLiteDb.dataTypes // ktlint-disable package-name
// ktlint-disable filename
// ktlint-disable filename

data class UserLogin(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val profileImage: String = "",
)
