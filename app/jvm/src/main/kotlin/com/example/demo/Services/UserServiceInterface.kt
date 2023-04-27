package com.example.demo.Services // ktlint-disable package-name

import com.example.demo.domain.LogInInfo
import com.example.demo.domain.UserImg
import domain.Response
import domain.User

interface UserServiceInterface {
    fun getUser(name: String): Response<User?>
    fun getUserByToken(token: String): Response<User?>
    fun getUsers(limit: Int, offset: Int): Response<List<User>>
    fun create(username: String, email: String, password: String): Response<User>
    fun updateUser(oldName: String, name: String, email: String, newPw: String): Response<User>
    fun checkToken(token: String): Response<Boolean>
    fun login(username: String?, email: String?, password: String): Response<LogInInfo?>
    fun logout(token: String): Response<Boolean>

    fun putUserImage(userImg: UserImg)
    fun getUserImage(name: String): Response<UserImg?>
}
