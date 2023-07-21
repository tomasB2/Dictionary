package com.example.dictionarywithcompose.SqlLiteDb.dataTypes // ktlint-disable package-name


data class UserLogin(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val profileImage: String = "",
)
