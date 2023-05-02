package com.example.demo.user.domain.outpus

import com.example.demo.user.domain.User

data class UserOut(
    val name: String,
    val email: String,
)

fun User.toUserOut() = UserOut(
    name = name,
    email = email,
)
