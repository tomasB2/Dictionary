package com.example.demo.http.outputStructures

import domain.User

data class UserOut(
    val name: String,
    val email: String
)

fun User.toUserOut() = UserOut(
    name = name,
    email = email
)
