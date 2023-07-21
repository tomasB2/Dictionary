package com.example.demo.chatRooms.repo

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.utils.UserQueris
import com.example.demo.common.repository.utils.getInDataBase
import com.example.demo.user.repo.UserMapper
import org.slf4j.LoggerFactory
import java.sql.Connection

class ChatRoomRepoSql(
    private val connection: Connection,
    //private val roomMapper: RoomMapper,
    //private val messageMapper: MessageMapper,
) : ChatRoomRepoInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createChat(data: ChatRoom): Response<ChatRoom?> {
        logger.info("getUserById for: {}", data.id)
        return try {
            val stm = connection
                .prepareStatement(UserQueris.getUserById)
            stm.setString(1,  data.id)
            val res = getInDataBase(
                stm = stm,
            )
            if (!res.next()) {
                Response(res = null, e = ErrorResponse(error = "User not found", cause = ErrorCause.USER_NOT_FOUND))
            } else {
                //Response(res = userMapper.map(res), e = null)
                Response(res = null, e = null)
            }
        } catch (e: Exception) {
            logger.error("getUserById for: {}, e: {}", data.id, e.message)
            throw e
        }
    }

    override fun updateChat(data: ChatRoom): Response<Boolean?> {
        TODO("Not yet implemented")
    }

    override fun getChat(chat_id: String): Response<ChatRoom?> {
        TODO("Not yet implemented")
    }

    override fun getChatRoomByUser(user_id: Int): Response<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override fun getChatMessages(chat_id: String): Response<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun getMessage(message_id: String): Response<Message?> {
        TODO("Not yet implemented")
    }

    override fun addMessage(message: Message): Response<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateMessage(message: Message): Response<Boolean> {
        TODO("Not yet implemented")
    }

}
