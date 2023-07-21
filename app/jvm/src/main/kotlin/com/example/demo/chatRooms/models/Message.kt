package com.example.demo.chatRooms.models

import java.sql.Timestamp
import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val from: Int, // username/id
    val to: String, // chat room id
    val text: String,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
    val read_by: List<Int>, // list of username/id
) {
    fun updateReadBy(user_id: Int): Message {
        val read_by = this.read_by.toMutableList()
        read_by.add(user_id)
        return this.copy(read_by = read_by)
    }
}
