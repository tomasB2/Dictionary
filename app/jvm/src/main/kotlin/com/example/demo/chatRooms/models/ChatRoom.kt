package com.example.demo.chatRooms.models

data class ChatRoom(
    val id: String,
    val name: String,
    val members: List<Int>,
)
