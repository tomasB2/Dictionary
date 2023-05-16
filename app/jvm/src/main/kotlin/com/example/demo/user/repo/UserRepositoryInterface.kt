package com.example.demo.user.repo

import com.example.demo.common.domain.Response
import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg

interface UserRepositoryInterface {
    fun getUserById(id: Int): Response<User?>
    fun getUserByName(name: String): Response<User?>
    fun getUserByEmail(email: String): Response<User?>
    fun getUserByToken(token: String): Response<User?>
    fun getListOfUsers(limit: Int, offset: Int): Response<List<User>>
    fun createUser(name: String, verify: String, email: String): Int
    fun editUser(id: Int, user: User)
    fun getToken(name: String): Response<String?>
    fun checkTokenValidity(token: String): Response<Boolean>
    fun createToken(id: Int, token: String)
    fun deleteToken(token: String)

    fun checkNameExistence(name: String): Response<Boolean>
    fun checkEmailExistence(email: String): Response<Boolean>
    fun getUserImage(id: Int): Response<UserImg?>
    fun putUserImage(userImg: UserImg)
}
