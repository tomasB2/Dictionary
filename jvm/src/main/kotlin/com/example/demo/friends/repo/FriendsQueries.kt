package com.example.demo.friends.repo

object FriendsQueries {

    const val CREATE_FRIENDS_LIST = "INSERT INTO friends_list (user_name, friends) VALUES (?, ?)"

    const val UPDATE_FRIENDS = "UPDATE friends_list SET friends = ? WHERE user_name = ?"

    const val GET_FRIENDS = "SELECT friends FROM friends_list WHERE user_name = ?"
}
