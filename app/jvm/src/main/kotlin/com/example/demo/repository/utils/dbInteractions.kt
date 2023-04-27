package repository.utils // ktlint-disable filename

import com.example.demo.domain.errors.ServerError
import com.example.demo.http.utils.Uris
import com.example.demo.repository.Transaction
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

fun createInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun updateInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun deleteInDataBase(stm: PreparedStatement) = stm.executeUpdate()

fun getInDataBase(stm: PreparedStatement): ResultSet {
    val rs = stm.executeQuery()
    return rs ?: throw ServerError(message = "An error occurred within the database", cause = null)
}

fun checkNewUserEmail(email: String, connection: Connection): Response<Boolean> {
    val stm = connection.prepareStatement(UserQueris.getUserByToken)
    stm.setString(1, email)
    val rs = getInDataBase(stm)
    if (rs.next()) {
        return Response(false, ErrorResponse(message = "Email already in use", cause = ErrorCause.EMAIL_ALREADY_EXISTS))
    }
    return Response(true, null)
}

fun checkNewUsername(username: String, connection: Connection): Response<Boolean> {
    val stm = connection.prepareStatement(UserQueris.getUserByName)
    stm.setString(1, username)
    val rs = getInDataBase(stm)
    if (rs.next()) {
        return Response(false, ErrorResponse(message = "Username already in use", cause = ErrorCause.USER_ALREADY_EXISTS))
    }
    return Response(true, null)
}

fun checkTokenUtil(token: String, transaction: Transaction): Response<Boolean> {
    try {
        val res = transaction.usersRepository.checkTokenValidity(token)
        if (!res.res) {
            transaction.usersRepository.deleteToken(token)
            return Response(false, ErrorResponse(message = "Invalid Token", cause = ErrorCause.TOKEN_NOT_FOUND))
        }
        return Response(true, null)
    } catch (e: Exception) {
        throw e
    }
}
