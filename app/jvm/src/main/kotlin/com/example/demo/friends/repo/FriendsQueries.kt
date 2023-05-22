package com.example.demo.friends.repo

object FriendsQueries {

    const val CREATE_FRIENDS_LIST = "INSERT INTO friends_list (user_id, friends, request) VALUES (?, ?, ?)"

    const val UPDATE_FRIENDS = "UPDATE friends_list SET friends = ? WHERE user_id = ?"

    const val GET_FRIENDS = "SELECT friends FROM friends_list WHERE user_id = ?"

    const val GET_FRIEND_REQUESTS = "SELECT request FROM friends_list WHERE user_id = ?"

    const val UPDATE_FROM_REQUESTS = "UPDATE friends_list SET request = ? WHERE user_id = ?"
}
