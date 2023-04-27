package repository.implementations

import com.example.demo.domain.UserImg
import com.example.demo.http.pipeline.AuthenticationInterceptor
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import domain.User
import org.slf4j.LoggerFactory
import repository.UserRepositoryInterface
import repository.implementations.mappers.UserMapper
import repository.utils.* // ktlint-disable no-wildcard-imports
import java.sql.Connection
import java.util.*

private val logger = LoggerFactory.getLogger(UserRepositoryImp::class.java)

private const val THIRTY_DAYS: Long = (60 * 60 * 24) * 30

class UserRepositoryImp(
    private val connection: Connection,
    private val userMapper: UserMapper,
) : UserRepositoryInterface {
    override fun getUserByName(name: String): Response<User?> {
        val stm = connection
            .prepareStatement(UserQueris.getUserByName)
        stm.setString(1, name)
        val res = getInDataBase(
            stm = stm,
        )
        if (!res.next()) {
            return Response(res = null, e = ErrorResponse(message = "User not found", cause = ErrorCause.USER_NOT_FOUND))
        }
        return Response(res = userMapper.map(res), e = null)
    }

    override fun getUserByEmail(email: String): Response<User?> {
        val stm = connection
            .prepareStatement(UserQueris.getUserByEmail)
        stm.setString(1, email)
        val res = getInDataBase(
            stm = stm,
        )
        if (!res.next()) {
            return Response(res = null, e = ErrorResponse(message = "Email not found", cause = ErrorCause.EMAIL_NOT_FOUND))
        }
        return Response(res = userMapper.map(res), e = null)
    }

    override fun getUserByToken(token: String): Response<User?> {
        val stm = connection
            .prepareStatement(UserQueris.getUserByToken)
        stm.setString(1, token)
        val res = getInDataBase(
            stm = stm,
        )
        logger.info("It got out")
        if (!res.next()) {
            return Response(res = null, e = ErrorResponse(message = "Token not found", cause = ErrorCause.TOKEN_NOT_FOUND))
        }
        logger.info("It got out")
        return Response(res = userMapper.map(res), e = null)
    }

    override fun getListOfUsers(limit: Int, offset: Int): Response<List<User>> {
        val stm = connection
            .prepareStatement(UserQueris.getListOfUsers)
        stm.setInt(1, limit)
        stm.setInt(2, offset)
        val res = getInDataBase(
            stm = stm,
        )
        val gameList = mutableListOf<User>()
        while (res.next()) {
            gameList.add(userMapper.map(res))
        }
        return Response(res = gameList, e = null)
    }

    override fun createUser(name: String, password_verification: String, email: String) {
        val stm = connection
            .prepareStatement(UserQueris.createUser)
        stm.setString(1, name)
        stm.setString(2, password_verification)
        stm.setString(3, email)
        createInDataBase(
            stm = stm,
        )
    }

    override fun editUser(oldName: String, user: User) {
        val stm = connection
            .prepareStatement(UserQueris.updateUser)
        stm.setString(1, user.name)
        stm.setString(2, user.password_verification)
        stm.setString(3, user.email)
        stm.setString(4, oldName)
        updateInDataBase(
            stm = stm,
        )
    }

    override fun getToken(name: String): Response<String?> {
        val stm = connection
            .prepareStatement("select tokenString from tokens where user_id = ?")
        stm.setString(1, name)
        val res = getInDataBase(
            stm = stm,
        )
        if (!res.next()) {
            return Response(res = null, e = null)
        }
        return Response(res = res.getString("tokenString"), e = null)
    }

    override fun checkTokenValidity(token: String): Response<Boolean> {
        val stm = connection
            .prepareStatement(UserQueris.checkTokenValidity)
        stm.setString(1, token)
        val res = getInDataBase(
            stm = stm,
        )
        if (!res.next()) return Response(res = false, e = null)
        val validity = res.getLong("validity")
        return Response(res = validity > Date().toInstant().epochSecond, e = null)
    }

    override fun createToken(name: String, token: String) {
        val stm = connection
            .prepareStatement(UserQueris.createToken)
        stm.setString(1, name)
        stm.setString(2, token)
        stm.setLong(3, Date().toInstant().plusSeconds(THIRTY_DAYS).epochSecond)
        updateInDataBase(
            stm = stm,
        )
    }

    override fun deleteToken(token: String) {
        val stm = connection
            .prepareStatement(UserQueris.deleteToken)
        stm.setString(1, token)
        deleteInDataBase(
            stm = stm,
        )
    }

    override fun checkNameExistence(name: String) = checkNewUsername(name, connection)

    override fun checkEmailExistence(email: String) = checkNewUserEmail(email, connection)
    override fun getUserImage(name: String): Response<UserImg?> {
        val stm = connection
            .prepareStatement(UserQueris.getUserImage)
        stm.setString(1, name)
        val res = getInDataBase(
            stm = stm,
        )
        if (!res.next()) {
            return Response(res = null, e = ErrorResponse(message = "User not found", cause = ErrorCause.USER_NOT_FOUND))
        }
        return Response(res = userMapper.mapImg(res), e = null)
    }

    override fun putUserImage(userImg: UserImg) {
        val stm = connection
            .prepareStatement(UserQueris.putUserImage)
        stm.setString(1, userImg.name)
        stm.setString(2, userImg.data)
        createInDataBase(
            stm = stm,
        )
    }
}
