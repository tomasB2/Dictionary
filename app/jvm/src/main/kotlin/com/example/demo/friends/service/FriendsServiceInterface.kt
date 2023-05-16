package com.example.demo.friends.service

import com.example.demo.common.domain.Response
import com.example.demo.user.domain.User

interface FriendsServiceInterface {
    fun addFriend(token: String, friendName: String): Response<Boolean>

    fun removeFriend(token: String, friendName: String): Response<Boolean>

    fun getFriends(userName: String): Response<List<User>?>

    fun getFriendRequests(token: String): Response<List<User>?>

    fun acceptFriendRequest(token: String, friendName: String): Response<Boolean>

    fun declineFriendRequest(token: String, friendName: String): Response<Boolean>
}
