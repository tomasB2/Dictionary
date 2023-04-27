package com.example.demo.Services.implementations // ktlint-disable package-name

import com.example.demo.Services.UserServiceInterface
import com.example.demo.Services.utils.encodeString
import com.example.demo.domain.LogInInfo
import com.example.demo.domain.UserImg
import com.example.demo.repository.implementations.TransactionManagerImp
import domain.ErrorCause
import domain.ErrorResponse
import domain.Response
import domain.User
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.*

@Suppress("unused")
@Component
class UserServices : UserServiceInterface {
    override fun getUser(name: String): Response<User?> {
        return TransactionManagerImp.run {
            it.usersRepository.getUserByName(name)
        }
    }

    override fun getUserByToken(token: String): Response<User?> {
        return TransactionManagerImp.run {
            it.usersRepository.getUserByToken(token)
        }
    }

    override fun getUsers(limit: Int, offset: Int): Response<List<User>> {
        return TransactionManagerImp.run {
            it.usersRepository.getListOfUsers(limit, offset)
        }
    }

    override fun create(username: String, email: String, password: String): Response<User> {
        return TransactionManagerImp.run {
            val password_verification = encodeString(password)
            val res = it.usersRepository.createUser(username, password_verification, email)
            val user = User(
                name = username,
                email = email,
                password_verification = password_verification,
            )
            Response(res = user, e = null)
        }
    }

    override fun updateUser(oldName: String, name: String, email: String, newPw: String): Response<User> {
        return TransactionManagerImp.run {
            val newUser = User(
                name = name,
                email = email,
                password_verification = encodeString(newPw),
            )
            it.usersRepository.editUser(oldName, newUser)
            Response(res = newUser, e = null)
        }
    }

    override fun checkToken(token: String): Response<Boolean> {
        return TransactionManagerImp.run {
            val res = it.usersRepository.checkTokenValidity(token)
            if (res.e != null) {
                return@run Response(res = false, e = res.e)
            }
            if (!res.res) {
                return@run Response(res = false, e = ErrorResponse(message = "Token is not valid", cause = ErrorCause.INVALID_TOKEN))
            }
            Response(res = true, e = null)
        }
    }

    override fun login(username: String?, email: String?, password: String): Response<LogInInfo?> {
        val password_verification = encodeString(password)
        return TransactionManagerImp.run {
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
            if (user.res?.password_verification == password_verification) {
                val token = UUID.randomUUID().toString()
                it.usersRepository.createToken(user.res.name, token)
                Response(LogInInfo(name = user.res.name, token = token), null)
            } else {
                Response(null, ErrorResponse(message = "Invalid password", cause = ErrorCause.WRONG_PASSWORD))
            }
        }
    }

    override fun logout(token: String): Response<Boolean> {
        return TransactionManagerImp.run {
            val validity = it.usersRepository.checkTokenValidity(token)
            if (validity.res) {
                it.usersRepository.deleteToken(token)
            }
            validity
        }
    }

    override fun putUserImage(userImg: UserImg) {
        TransactionManagerImp.run {
            it.usersRepository.putUserImage(userImg)
        }
    }

    override fun getUserImage(name: String): Response<UserImg?> {
        return TransactionManagerImp.run {
            it.usersRepository.getUserImage(name)
        }
    }
}
