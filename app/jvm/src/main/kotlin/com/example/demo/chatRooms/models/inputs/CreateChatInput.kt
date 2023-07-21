package com.example.demo.chatRooms.models.inputs

data class CreateChatInput(
    val name: String,
    val members: List<String>,
)
