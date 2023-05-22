package com.example.demo.friends.repo

import com.example.demo.common.domain.Response
import com.example.demo.friends.FriendRequests
import com.example.demo.friends.Friends

interface FriendsRepoInterface {
    fun createFriendsList(userId: Int, friendList: Friends): Response<Boolean>

    fun updateFriendsList(userId: Int, friendList: Friends): Response<Boolean>

    fun getFriends(userId: Int): Response<Friends?>

    fun getFriendRequests(userId: Int): Response<FriendRequests?>

    fun updateRequests(userId: Int, requests: FriendRequests): Response<Boolean>
}
