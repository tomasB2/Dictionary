package com.example.demo.friends

import com.fasterxml.jackson.databind.ObjectMapper

data class Friends(
    val list: MutableList<String>,
) {

    fun addFriend(user: String) {
        list.add(user)
    }

    fun removeFriend(user: String) {
        list.remove(user)
    }

    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}

fun friendsFromJson(json: String): Friends {
    val objectMapper = ObjectMapper()
    return objectMapper.readValue(json, Friends::class.java)
}
