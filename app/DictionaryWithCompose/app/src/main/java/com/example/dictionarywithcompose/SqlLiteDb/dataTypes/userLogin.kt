package com.example.dictionarywithcompose.SqlLiteDb.dataTypes

data class UserLogin(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val profileImage: String = "",
)