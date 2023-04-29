package com.example.demo.chatRooms.models

import java.sql.Timestamp

data class Message(
    val id: String,
    val from: String, // username/id
    val to: String, // chat room id
    val text: String,
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
    val read_by: List<String>, // list of username/id
)
