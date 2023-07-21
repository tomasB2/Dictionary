package com.example.demo.user.repo

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.utils.* // ktlint-disable no-wildcard-imports
import com.example.demo.user.domain.Types
import com.example.demo.user.domain.Types.Companion.toBoolean
import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.Statement
import java.util.*

private const val THIRTY_DAYS: Long = (60 * 60 * 24) * 30

class UserRepositoryImp(
    private val connection: Connection,
    private val userMapper: UserMapper,
) : UserRepositoryInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getUserById(id: Int): Response<User?> {
        logger.info("getUserById for: {}", id)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserById)
            stm.setInt(1, id)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
            } else {
                Response(res = userMapper.map(res), e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserById for: {}, e: {}", id, e.message)
            throw e
        }
    }

    override fun getUserByName(name: String): Response<User?> {
        logger.info("getUserByName for: {}", name)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserByName)
            stm.setString(1, name)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
            } else {
                Response(res = userMapper.map(res), e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserByName for: {}, e: {}", name, e.message)
            throw e
        }
    }

    override fun getUserByEmail(email: String): Response<User?> {
        logger.info("getUserByEmail for: {}", email)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserByEmail)
            stm.setString(1, email)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "Email not found", cause = ErrorCause.EMAIL_NOT_FOUND))
            } else {
                Response(res = userMapper.map(res), e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserByEmail for: {}, e: {}", email, e.message)
            throw e
        }
    }

    override fun getUserByToken(token: String): Response<User?> {
        logger.info("getUserByToken for: {}", token)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserByToken)
            stm.setString(1, token)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "Token not found", cause = ErrorCause.TOKEN_NOT_FOUND))
            } else {
                Response(res = userMapper.map(res), e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserByToken for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun getListOfUsers(limit: Int, offset: Int): Response<List<User>> {
        logger.info("getListOfUsers for: {}, {}", limit, offset)
        return try {
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
            Response(res = gameList, e = null)
        } catch (e: Exception) {
            logger.error("getListOfUsers for: {}, {}, e: {}", limit, offset, e.message)
            throw e
        }
    }

    override fun createUser(name: String, verify: String, email: String, type: Types): Int {
        logger.info("createUser for: {}, {}, {}", name, verify, email)
        try {
            val stm = connection
                .prepareStatement(
                    UserQueris.createUser,
                    Statement.RETURN_GENERATED_KEYS,
                )
            stm.setString(1, name)
            stm.setString(2, verify)
            stm.setString(3, email)
            stm.setBoolean(4, type.toBoolean())
            return createInDataBaseWithId(
                stm = stm,
            )
        } catch (e: Exception) {
            logger.error("createUser for: {}, {}, {}, e: {}", name, verify, email, e.message)
            throw e
        }
    }

    override fun editUser(id: Int, user: User) {
        logger.info("editUser for: {}, {}", id, user)
        try {
            val stm = connection
                .prepareStatement(UserQueris.updateUser)
            stm.setString(1, user.name)
            stm.setString(2, user.verify)
            stm.setString(3, user.email)
            stm.setInt(4, id)
            updateInDataBase(
                stm = stm,
            )
        } catch (e: Exception) {
            logger.error("editUser for: {}, {}, e: {}", id, user, e.message)
            throw e
        }
    }

    override fun getToken(name: String): Response<String?> {
        logger.info("getToken for: {}", name)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getToken)
            stm.setString(1, name)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = null)
            } else {
                Response(res = res.getString("tokenString"), e = null)
            }
        } catch (e: Exception) {
            logger.error("getToken for: {}, e: {}", name, e.message)
            throw e
        }
    }

    override fun checkTokenValidity(token: String): Response<Boolean> {
        logger.info("checkTokenValidity for: {}", token)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.checkTokenValidity)
            stm.setString(1, token)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) return Response(res = false, e = null)
            val validity = res.getLong("validity")
            Response(res = validity > Date().toInstant().epochSecond, e = null)
        } catch (e: Exception) {
            logger.error("checkTokenValidity for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun createToken(id: Int, token: String) {
        logger.info("createToken for: {}, {}", id, token)
        try {
            val stm = connection
                .prepareStatement(UserQueris.createToken)
            stm.setInt(1, id)
            stm.setString(2, token)
            stm.setLong(3, Date().toInstant().plusSeconds(THIRTY_DAYS).epochSecond)
            updateInDataBase(
                stm = stm,
            )
        } catch (e: Exception) {
            logger.error("createToken for: {}, {}, e: {}", id, token, e.message)
            throw e
        }
    }

    override fun deleteToken(token: String) {
        logger.info("deleteToken for: {}", token)
        try {
            val stm = connection
                .prepareStatement(UserQueris.deleteToken)
            stm.setString(1, token)
            deleteInDataBase(
                stm = stm,
            )
        } catch (e: Exception) {
            logger.error("deleteToken for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun checkNameExistence(name: String): Response<Boolean> {
        logger.info("checkNameExistence for: {}", name)
        return try {
            checkNewUsername(name, connection)
        } catch (e: Exception) {
            logger.error("checkNameExistence for: {}, e: {}", name, e.message)
            throw e
        }
    }

    override fun checkEmailExistence(email: String): Response<Boolean> {
        logger.info("checkEmailExistence for: {}", email)
        return try {
            checkNewUserEmail(email, connection)
        } catch (e: Exception) {
            logger.error("checkEmailExistence for: {}, e: {}", email, e.message)
            throw e
        }
    }

    override fun getUserImage(id: Int): Response<UserImg?> {
        logger.info("getUserImage for: {}", id)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserImage)
            stm.setInt(1, id)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
            } else {
                Response(res = userMapper.mapImg(res), e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserImage for: {}, e: {}", id, e.message)
            throw e
        }
    }

    override fun putUserImage(userImg: UserImg) {
        logger.info("putUserImage for: {}", userImg)
        try {
            val stm = connection
                .prepareStatement(UserQueris.putUserImage)
            stm.setInt(1, userImg.id)
            stm.setString(2, userImg.data)
            createInDataBase(
                stm = stm,
            )
        } catch (e: Exception) {
            logger.error("putUserImage for: {}, e: {}", userImg, e.message)
            throw e
        }
    }
}
