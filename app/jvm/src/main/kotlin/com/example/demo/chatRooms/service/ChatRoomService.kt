package com.example.demo.chatRooms.service

import com.example.demo.chatRooms.models.ChatRoom
import com.example.demo.chatRooms.models.Message
import com.example.demo.chatRooms.models.inputs.CreateChatInput
import com.example.demo.chatRooms.models.inputs.UpdateChatRoomInput
import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.Response
import com.example.demo.common.repository.implementations.TransactionManagerImp
import org.slf4j.LoggerFactory
import java.util.*

class ChatRoomService : ChatRoomServicesInterface {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createChat(token: String, data: CreateChatInput): Response<ChatRoom?> {
        logger.info("createChat for: {} and {}", token, data)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = ChatRoom(
                    id = UUID.randomUUID().toString(),
                    name = data.name,
                    members = listOf(user.id),
                )
                it.roomsRepository.createChat(room)
                return@run Response(room, e = null)
            }
        } catch (e: Exception) {
            logger.error("createChat for: {} and {}, e: {}", token, data, e.message)
            throw e
        }
    }

    override fun getChat(token: String, chat_id: String): Response<ChatRoom?> {
        logger.info("getChat for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    return@run Response(room, e = null)
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("getChat for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }

    override fun addUserToChat(token: String, chat_id: String, user_id: String): Response<Boolean?> {
        logger.info("addUserToChat for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User already in chat",
                            cause = ErrorCause.USER_ALREADY_IN_CHAT,
                        ),
                    )
                } else {
                    val newRoom = room.copy(members = room.members + user.id)
                    return@run it.roomsRepository.updateChat(newRoom)
                }
            }
        } catch (e: Exception) {
            logger.error("addUserToChat for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }

    override fun removeUserFromChat(token: String, chat_id: String, user_id: String): Response<Boolean?> {
        logger.info("removeUserFromChat for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    val newRoom = room.copy(members = room.members - user.id)
                    return@run it.roomsRepository.updateChat(newRoom)
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("removeUserFromChat for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }

    override fun getChatRoomByUser(token: String): Response<List<ChatRoom>?> {
        logger.info("getChatRoomByUser for: {}", token)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val rooms = it.roomsRepository.getChatRoomByUser(user.id).res
                return@run Response(rooms, e = null)
            }
        } catch (e: Exception) {
            logger.error("getChatRoomByUser for: {}, e: {}", token, e.message)
            throw e
        }
    }

    override fun updateChat(token: String, chat_id: String, data: UpdateChatRoomInput): Response<Boolean?> {
        logger.info("updateChat for: {} and {}", token, data)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    val updatedRoom = room.copy(name = data.name)
                    return@run it.roomsRepository.updateChat(updatedRoom)
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("updateChat for: {} and {}, e: {}", token, data, e.message)
            throw e
        }
    }

    override fun getChatMessages(token: String, chat_id: String): Response<List<Message>?> {
        logger.info("getChatMessages for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    val messages = it.roomsRepository.getChatMessages(chat_id).res
                    return@run Response(messages, e = null)
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("getChatMessages for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }

    override fun addMessage(token: String, chat_id: String, text: String): Response<Boolean?> {
        logger.info("addMessage for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    val message = Message(
                        to = chat_id,
                        from = user.id,
                        text = text,
                        read_by = listOf(user.id),
                    )
                    it.roomsRepository.addMessage(message)
                    return@run Response(true, e = null)
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("addMessage for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }

    override fun viewMessage(token: String, chat_id: String, message_id: String): Response<Boolean?> {
        logger.info("viewMessage for: {} and {}", token, chat_id)
        return try {
            TransactionManagerImp.run {
                val user = it.usersRepository.getUserByToken(token).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not found",
                            cause = ErrorCause.USER_NOT_FOUND,
                        ),
                    )
                val room = it.roomsRepository.getChat(chat_id).res
                    ?: return@run Response(
                        null,
                        ErrorResponse(
                            error = "Chat not found",
                            cause = ErrorCause.CHAT_NOT_FOUND,
                        ),
                    )
                if (room.members.contains(user.id)) {
                    val message = it.roomsRepository.getMessage(message_id).res
                        ?: return@run Response(
                            null,
                            ErrorResponse(
                                error = "Message not found",
                                cause = ErrorCause.MESSAGE_NOT_FOUND,
                            ),
                        )
                    if (message.to == chat_id) {
                        if (!message.read_by.contains(user.id)) {
                            message.updateReadBy(user.id)
                            it.roomsRepository.updateMessage(message)
                        }
                        return@run Response(true, e = null)
                    } else {
                        return@run Response(
                            null,
                            ErrorResponse(
                                error = "Message not in chat",
                                cause = ErrorCause.MESSAGE_NOT_IN_CHAT,
                            ),
                        )
                    }
                } else {
                    return@run Response(
                        null,
                        ErrorResponse(
                            error = "User not in chat",
                            cause = ErrorCause.USER_NOT_IN_CHAT,
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("viewMessage for: {} and {}, e: {}", token, chat_id, e.message)
            throw e
        }
    }
}
