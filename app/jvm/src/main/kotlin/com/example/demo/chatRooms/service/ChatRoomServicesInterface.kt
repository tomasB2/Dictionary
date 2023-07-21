package com.example.demo.chatRooms.service

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.chatRooms.models.inputs.CreateChatInput
import com.example.demo.chatRooms.models.inputs.UpdateChatRoomInput
import com.example.demo.common.domain.Response

interface ChatRoomServicesInterface {

    fun createChat(token: String, data: CreateChatInput): Response<ChatRoom?>

    fun getChat(token: String, chat_id: String): Response<ChatRoom?>

    fun addUserToChat(token: String, chat_id: String, user_id: String): Response<Boolean?>

    fun removeUserFromChat(token: String, chat_id: String, user_id: String): Response<Boolean?>

    fun getChatRoomByUser(token: String): Response<List<ChatRoom>?>

    fun updateChat(token: String, chat_id: String, data: UpdateChatRoomInput): Response<Boolean?>

    fun getChatMessages(token: String, chat_id: String): Response<List<Message>?>

    fun addMessage(token: String, chat_id: String, text: String): Response<Boolean?>

    fun viewMessage(token: String, chat_id: String, message_id: String): Response<Boolean?>
}
