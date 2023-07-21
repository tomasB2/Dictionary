package com.example.demo.common.repository.utils // ktlint-disable filename

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.domain.errors.ServerError
import com.example.demo.common.repository.Transaction
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

fun createInDataBaseWithId(stm: PreparedStatement): Int {
    stm.executeUpdate()
    val rs = stm.generatedKeys
    if (!rs.next()) {
        throw ServerError(
            message = "An error occurred within the database",
            cause = null,
        )
    }
    return rs.getInt("id")
}

fun createInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun updateInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun deleteInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun getInDataBase(stm: PreparedStatement): ResultSet {
    val rs = stm.executeQuery()
    return rs ?: throw com.example.demo.common.domain.errors.ServerError(
        message = "An error occurred within the database",
        cause = null,
    )
}

fun checkNewUserEmail(email: String, connection: Connection): Response<Boolean> {
    val stm = connection.prepareStatement(UserQueris.getUserByToken)
    stm.setString(1, email)
    val rs = getInDataBase(stm)
    if (rs.next()) {
        return Response(false, ErrorResponse(error = "Email already in use", cause = ErrorCause.EMAIL_ALREADY_EXISTS))
    }
    return Response(true, null)
}

fun checkNewUsername(username: String, connection: Connection): Response<Boolean> {
    val stm = connection.prepareStatement(UserQueris.getUserByName)
    stm.setString(1, username)
    val rs = getInDataBase(stm)
    if (rs.next()) {
        return Response(false, ErrorResponse(error = "Username already in use", cause = ErrorCause.USER_ALREADY_EXISTS))
    }
    return Response(true, null)
}

fun checkTokenUtil(token: String, transaction: Transaction): Response<Boolean> {
    try {
        val res = transaction.usersRepository.checkTokenValidity(token)
        if (!res.res) {
            transaction.usersRepository.deleteToken(token)
            return Response(false, ErrorResponse(error = "Invalid Token", cause = ErrorCause.TOKEN_NOT_FOUND))
        }
        return Response(true, null)
    } catch (e: Exception) {
        throw e
    }
}
