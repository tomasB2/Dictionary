package com.example.demo.friends.repo

import com.example.demo.friends.Friends
import com.example.demo.friends.friendsFromJson
import java.sql.ResultSet

class FriendsMapper {
    fun map(rs: ResultSet): Friends {
        val friends = rs.getString("friends")
        return Friends(
            list = friendsFromJson(friends).list,
        )
    }
}
