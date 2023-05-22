package com.example.demo.friends.repo

import com.example.demo.friends.* // ktlint-disable no-wildcard-imports
import java.sql.ResultSet

class FriendsMapper {
    fun mapFriends(rs: ResultSet): Friends? {
        val friends = rs.getString("friends")
        return friendsFromJson(friends)?.let {
            Friends(
                list = it.list,
            )
        }
    }

    fun mapRequests(rs: ResultSet): FriendRequests? {
        val requests = rs.getString("request")
        return requestFromJson(requests)?.let {
            FriendRequests(
                list = it.list,
            )
        }
    }
}
