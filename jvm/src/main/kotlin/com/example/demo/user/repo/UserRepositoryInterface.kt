package com.example.demo.user.repo

import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg
import com.example.demo.common.domain.Response

interface UserRepositoryInterface {
    fun getUserByName(name: String): Response<User?>
    fun getUserByEmail(email: String): Response<User?>
    fun getUserByToken(token: String): Response<User?>
    fun getListOfUsers(limit: Int, offset: Int): Response<List<User>>
    fun createUser(name: String, verify: String, email: String)
    fun editUser(oldName: String, user: User)
    fun getToken(name: String): Response<String?>
    fun checkTokenValidity(token: String): Response<Boolean>
    fun createToken(name: String, token: String)
    fun deleteToken(token: String)

    fun checkNameExistence(name: String): Response<Boolean>
    fun checkEmailExistence(email: String): Response<Boolean>

    fun getUserImage(name: String): Response<UserImg?>
    fun putUserImage(userImg: UserImg)
}
