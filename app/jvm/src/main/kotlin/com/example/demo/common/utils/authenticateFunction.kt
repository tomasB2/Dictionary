package com.example.demo.common.utils // ktlint-disable filename

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.Transaction
import com.example.demo.user.domain.User

fun authenticate(bearer: String, transaction: Transaction): Response<User?> {
    val token = bearer.replace("Bearer ", "")
    if (!transaction.usersRepository.checkTokenValidity(token).res) {
        return Response(res = null, e = ErrorResponse(error = "Token not valid", cause = ErrorCause.TOKEN_NOT_VALID))
    }
    val user = transaction.usersRepository.getUserByToken(token)
    return if (user.e != null) {
        Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
    } else {
        user
    }
}
