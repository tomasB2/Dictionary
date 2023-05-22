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

    fun contains(user: String): Boolean {
        return list.contains(user)
    }

    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}

fun friendsFromJson(json: String): Friends? {
    return namesFromJson(json).let {
        if (it.isEmpty()) {
            null
        } else {
            Friends(it.toMutableList())
        }
    }
}

data class FriendRequests(
    val list: MutableList<String>,
) {

    fun addRequest(user: String) {
        list.add(user)
    }

    fun removeRequest(user: String) {
        list.remove(user)
    }

    fun contains(user: String): Boolean {
        return list.contains(user)
    }

    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}

fun requestFromJson(json: String): FriendRequests? {
    return namesFromJson(json).let {
        if (it.isEmpty()) {
            null
        } else {
            FriendRequests(it.toMutableList())
        }
    }
}

private fun namesFromJson(json: String): List<String> {
    if (json == "[]") return emptyList()
    val replacedJson = json
        .replace("{\"list\":[", "")
        .replace("\"", "")
        .replace("]}", "")
    val namesList = mutableListOf<String>()
    replacedJson.split(",").let {
        it.forEach { name ->
            if (it.isNotEmpty()) namesList.add(name)
        }
    }
    return namesList
}
