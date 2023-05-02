package com.example.demo.chatRooms.service

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.common.domain.Response

interface ChatRoomServicesInterface {

    fun createChat(): Response<ChatRoom>

    fun getChat(chat_id: Int): Response<ChatRoom>

    fun getChatMessages(chat_id: Int): Response<List<Message>>

    fun addMessage(chat_id: Int, message: Message): Response<Boolean>

    fun viewMessage(chat_id: Int, message_id: Int): Response<Boolean>
}
