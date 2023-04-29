package com.example.demo.friends.repo

import com.example.demo.friends.Friends
import com.example.demo.common.domain.Response

interface FriendsRepoInterface {
    fun createFriendsList(userName: String, friendList: Friends): Response<Boolean>

    fun updateList(userName: String, friendList: Friends): Response<Boolean>

    fun getFriends(userName: String): Response<Friends>
}
