package com.example.demo.chatRooms.repo

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.common.domain.Response

interface ChatRoomRepoInterface {

    fun createChat(data: ChatRoom): Response<ChatRoom?>

    fun updateChat(data: ChatRoom): Response<Boolean?>

    fun getChat(chat_id: String): Response<ChatRoom?>

    fun getChatRoomByUser(user_id: Int): Response<List<ChatRoom>>

    fun getChatMessages(chat_id: String): Response<List<Message>>

    fun getMessage(message_id: String): Response<Message?>

    fun addMessage(message: Message): Response<Boolean>

    fun updateMessage(message: Message): Response<Boolean>
}
