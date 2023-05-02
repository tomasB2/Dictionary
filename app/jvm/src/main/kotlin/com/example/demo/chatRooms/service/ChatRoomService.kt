package com.example.demo.chatRooms.service

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.common.domain.Response

class ChatRoomService : ChatRoomServicesInterface {
    override fun createChat(): Response<ChatRoom> {
        TODO("Not yet implemented")
    }

    override fun getChat(chat_id: Int): Response<ChatRoom> {
        TODO("Not yet implemented")
    }

    override fun getChatMessages(chat_id: Int): Response<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun addMessage(chat_id: Int, message: Message): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override fun viewMessage(chat_id: Int, message_id: Int): Response<Boolean> {
        TODO("Not yet implemented")
    }
}