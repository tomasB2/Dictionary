package com.example.demo.user.service // ktlint-disable package-name

import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.utils.encodeString
import com.example.demo.user.domain.LogInInfo
import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg
import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Suppress("unused")
@Component
class UserServices : UserServiceInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getUser(name: String): Response<User?> {
        logger.info("getUser for: {}", name)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.getUserByName(name)
            }
        } catch (e: Exception) {
            logger.error("getUser for: {}, e: {}", name, e.message)
            throw e
        }
    }

    override fun getUserByToken(token: String): Response<User?> {
        logger.info("getUserByToken for: {}", token)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.getUserByToken(token)
            }
        } catch (e: Exception) {
            logger.error("getUserByToken for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun getUsers(limit: Int, offset: Int): Response<List<User>> {
        logger.info("getUsers for: {} and {}", limit, offset)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.getListOfUsers(limit, offset)
            }
        } catch (e: Exception) {
            logger.error("getUsers for: {} and {}, e: {}", limit, offset, e.message)
            throw e
        }
    }

    override fun create(username: String, email: String, password: String): Response<User> {
        logger.info("create for: {}, {}, {}", username, email, password)
        return try {
            TransactionManagerImp.run {
                val verify = encodeString(password)
                it.usersRepository.createUser(username, verify, email)
                val user = User(
                    name = username,
                    email = email,
                    verify = verify,
                )
                Response(res = user, e = null)
            }
        } catch (e: Exception) {
            logger.error("Error creating user: {}, e: {}", username, e.message)
            throw e
        }
    }

    override fun updateUser(oldName: String, name: String, email: String, newPw: String): Response<User> {
        logger.info("updateUser for: {}, {}, {}, {}", oldName, name, email, newPw)
        return try {
            TransactionManagerImp.run {
                val newUser = User(
                    name = name,
                    email = email,
                    verify = encodeString(newPw),
                )
                it.usersRepository.editUser(oldName, newUser)
                Response(res = newUser, e = null)
            }
        } catch (e: Exception) {
            logger.error("Error updating user: {}, e: {}", oldName, e.message)
            throw e
        }
    }

    override fun checkToken(token: String): Response<Boolean> {
        logger.info("checkToken for: {}", token)
        return try {
            TransactionManagerImp.run {
                val res = it.usersRepository.checkTokenValidity(token)
                if (res.e != null) {
                    return@run Response(res = false, e = res.e)
                }
                if (!res.res) {
                    return@run Response(res = false, e = ErrorResponse(message = "Token is not valid", cause = ErrorCause.INVALID_TOKEN))
                }
                Response(res = true, e = null)
            }
        } catch (e: Exception) {
            logger.error("Error checking token: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun login(username: String?, email: String?, password: String): Response<LogInInfo?> {
        val verify = encodeString(password)
        logger.info("login for: {}, {}, {}", username, email, verify)
        return try {
            TransactionManagerImp.run {
                if (username == null && email == null) {
                    return@run Response(null, ErrorResponse(message = "Username or email is required", cause = ErrorCause.USER_BAD_REQUEST))
                }
                val user = if (username != null) {
                    it.usersRepository.getUserByName(username)
                } else if (email != null) {
                    it.usersRepository.getUserByEmail(email)
                } else {
                    throw Error("This should never happen")
                }
                if (!user.isSuccessful) {
                    return@run Response(null, user.e)
                }
                if (user.res?.verify == verify) {
                    val token = UUID.randomUUID().toString()
                    it.usersRepository.createToken(user.res.name, token)
                    Response(LogInInfo(name = user.res.name, token = token), null)
                } else {
                    Response(null, ErrorResponse(message = "Invalid password", cause = ErrorCause.WRONG_PASSWORD))
                }
            }
        } catch (e: Exception) {
            logger.error("Error logging in: {}, e: {}", username, e.message)
            throw e
        }
    }

    override fun logout(token: String): Response<Boolean> {
        logger.info("logout for: {}", token)
        return try {
            TransactionManagerImp.run {
                val validity = it.usersRepository.checkTokenValidity(token)
                if (validity.res) {
                    it.usersRepository.deleteToken(token)
                }
                validity
            }
        } catch (e: Exception) {
            logger.error("Error logging out: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun putUserImage(userImg: UserImg) {
        logger.info("putUserImage for: {}", userImg.name)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.putUserImage(userImg)
            }
        } catch (e: Exception) {
            logger.error("Error putting user image: {}, e: {}", userImg.name, e.message)
            throw e
        }
    }

    override fun getUserImage(name: String): Response<UserImg?> {
        logger.info("getUserImage for: {}", name)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.getUserImage(name)
            }
        } catch (e: Exception) {
            logger.error("Error getting user image: {}, e: {}", name, e.message)
            throw e
        }
    }
}
