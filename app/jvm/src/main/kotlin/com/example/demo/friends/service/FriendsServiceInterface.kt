package com.example.demo.friends.service

import com.example.demo.user.domain.User
import com.example.demo.common.domain.Response

interface FriendsServiceInterface {
    fun addFriend(userName: String, friendName: String): Response<Boolean>

    fun removeFriend(userName: String, friendName: String): Response<Boolean>

    fun getFriends(userName: String): Response<List<User>?>
}
