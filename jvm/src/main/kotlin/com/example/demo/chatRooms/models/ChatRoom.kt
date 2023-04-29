package com.example.demo.chatRooms.models

import com.example.demo.user.domain.User

data class ChatRoom(
    val id: String,
    val name: String,
    val members: List<User>,
)
