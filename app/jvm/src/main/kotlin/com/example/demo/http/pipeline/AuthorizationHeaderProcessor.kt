package com.example.demo.http.pipeline

import com.example.demo.Services.implementations.UserServices
import domain.User
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderProcessor(
    val userService: UserServices,
) {

    fun process(authorizationValue: String?): User? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return userService.getUserByToken(parts[1]).res
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
