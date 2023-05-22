package com.example.demo.user.service // ktlint-disable package-name

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.repository.utils.getInDataBase
import com.example.demo.common.utils.* // ktlint-disable no-wildcard-imports
import com.example.demo.friends.Friends
import com.example.demo.user.domain.LogInInfo
import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg
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
                val res = it.usersRepository.getUserByName(name)
                logger.info(res.res.toString())
                res
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

    override fun create(username: String, email: String, password: String): Response<User?> {
        logger.info("create for: {}, {}, {}", username, email, password)
        password.isValidPassword()
        return try {
            TransactionManagerImp.run {
                if (it.usersRepository.getUserByName(username).res != null) {
                    return@run Response(res = null, e = ErrorResponse(error = "Username already taken", cause = ErrorCause.USERNAME_TAKEN))
                }
                if (it.usersRepository.getUserByEmail(email).res != null) {
                    return@run Response(res = null, e = ErrorResponse(error = "Email already taken", cause = ErrorCause.EMAIL_TAKEN))
                }
                val verify = encodeString(password)
                val id = it.usersRepository.createUser(username, verify, email)
                val user = User(
                    id = id,
                    name = username,
                    email = email,
                    verify = verify,
                )
                it.friendsRepository.createFriendsList(user.id, Friends(mutableListOf<String>()))
                Response(res = user, e = null)
            }
        } catch (e: Exception) {
            logger.error("Error creating user: {}, e: {}", username, e.message)
            throw e
        }
    }

    override fun updateUser(token: String, name: String, email: String, newPw: String): Response<User?> {
        logger.info("updateUser for: {}, {}, {}, {}", token, name, email, newPw)
        return try {
            name.isValidName()
            email.isValidEmail()
            newPw.isValidPassword()
            TransactionManagerImp.run {
                val user = authenticate(token, it)
                if (user.res == null) return@run user
                val id = user.res.id
                val newUser = User(
                    id = id,
                    name = name,
                    email = email,
                    verify = encodeString(newPw),
                )
                it.usersRepository.editUser(id, newUser)
                Response(res = newUser, e = null)
            }
        } catch (e: Exception) {
            logger.error("Error updating user: {}, e: {}", token, e.message)
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
                    return@run Response(res = false, e = ErrorResponse(error = "Token is not valid", cause = ErrorCause.INVALID_TOKEN))
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
                    return@run Response(null, ErrorResponse(error = "Username or email is required", cause = ErrorCause.USER_BAD_REQUEST))
                }
                val user = if (username != null) {
                    it.usersRepository.getUserByName(username)
                } else if (email != null) {
                    it.usersRepository.getUserByEmail(email)
                } else {
                    throw Error("This should never happen")
                }
                if (!user.isSuccessful) {
                    return@run Response(null, ErrorResponse(error = "Invalid username or password", cause = ErrorCause.USER_UNAUTHORIZED))
                }
                if (user.res?.verify == verify) {
                    val token = UUID.randomUUID().toString()
                    it.usersRepository.createToken(user.res.id, token)
                    Response(LogInInfo(name = user.res.name, token = token), null)
                } else {
                    Response(null, ErrorResponse(error = "Invalid username or password", cause = ErrorCause.WRONG_PASSWORD))
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
        logger.info("putUserImage for: {}", userImg.id)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.putUserImage(userImg)
            }
        } catch (e: Exception) {
            logger.error("Error putting user image: {}, e: {}", userImg.id, e.message)
            throw e
        }
    }

    override fun getUserImage(id: Int): Response<UserImg?> {
        logger.info("getUserImage for: {}", id)
        return try {
            TransactionManagerImp.run {
                it.usersRepository.getUserImage(id)
            }
        } catch (e: Exception) {
            logger.error("Error getting user image: {}, e: {}", id, e.message)
            throw e
        }
    }
}
