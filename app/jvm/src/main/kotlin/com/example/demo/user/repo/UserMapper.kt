package com.example.demo.user.repo

import com.example.demo.user.domain.User
import com.example.demo.user.domain.UserImg
import java.sql.ResultSet

/**
 * Class for mapping user data from database to User object
 */
class UserMapper {
    fun map(rs: ResultSet): User {
        return User(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            verify = rs.getString("password_verification"),
            email = rs.getString("email"),
        )
    }

    fun mapImg(rs: ResultSet): UserImg {
        return UserImg(
            id = rs.getInt("name_id"),
            data = rs.getString("data"),
        )
    }
}
