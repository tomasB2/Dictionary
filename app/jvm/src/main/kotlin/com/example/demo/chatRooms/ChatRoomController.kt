package com.example.demo.chatRooms

import com.example.demo.chatRooms.models.inputs.*
import com.example.demo.chatRooms.service.ChatRoomServicesInterface
import com.example.demo.common.http.outputStructures.toBooleanOut
import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

class ChatRoomController(
    private val chatRoomServices: ChatRoomServicesInterface,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(Uris.ChatRooms.GET_CHAT_ROOMS)
    fun getChatRooms(
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<*> {
        logger.info("getChatRooms for: {}", token)
        val resp = chatRoomServices.getChatRoomByUser(token)
        return responseGenerator(resp, Uris.ChatRooms.GET_CHAT_ROOMS) {
            resp.res
        }
    }

    @PostMapping(Uris.ChatRooms.CREATE_CHAT_ROOM)
    fun createChatRoom(
        @RequestHeader("Authorization") token: String,
        @RequestBody data: CreateChatInput,
    ): ResponseEntity<*> {
        logger.info("createChatRoom for: {}", token)
        val resp = chatRoomServices.createChat(token, data)
        return responseGenerator(resp, Uris.ChatRooms.CREATE_CHAT_ROOM) {
            resp.res
        }
    }

    @PutMapping(Uris.ChatRooms.UPDATE_CHAT_ROOM)
    fun updateChatRoom(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody data: UpdateChatRoomInput,
    ): ResponseEntity<*> {
        logger.info("updateChatRoom for: {}", token)
        val resp = chatRoomServices.updateChat(token, id, data)
        return responseGenerator(resp, Uris.ChatRooms.UPDATE_CHAT_ROOM) {
            resp.res
        }
    }

    @GetMapping(Uris.ChatRooms.GET_CHAT_ROOM)
    fun getChatRoom(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
    ): ResponseEntity<*> {
        logger.info("getChatRoom for: {}", token)
        val resp = chatRoomServices.getChat(token, id)
        return responseGenerator(resp, Uris.ChatRooms.GET_CHAT_ROOM) {
            resp.res
        }
    }

    @PutMapping(Uris.ChatRooms.ADD_USER_TO_CHAT_ROOM)
    fun addUserToChatRoom(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody data: UserIdInput,
    ): ResponseEntity<*> {
        logger.info("addUserToChatRoom for: {}", token)
        val resp = chatRoomServices.addUserToChat(token, id, data.id)
        return responseGenerator(resp, Uris.ChatRooms.ADD_USER_TO_CHAT_ROOM) {
            resp.res
        }
    }

    @DeleteMapping(Uris.ChatRooms.REMOVE_USER_FROM_CHAT_ROOM)
    fun removeUserFromChatRoom(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody data: UserIdInput,
    ): ResponseEntity<*> {
        logger.info("removeUserFromChatRoom for: {}", token)
        val resp = chatRoomServices.removeUserFromChat(token, id, data.id)
        return responseGenerator(resp, Uris.ChatRooms.REMOVE_USER_FROM_CHAT_ROOM) {
            resp.res
        }
    }

    @GetMapping(Uris.ChatRooms.GET_CHAT_ROOM_MESSAGES)
    fun getChatRoomMessages(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
    ): ResponseEntity<*> {
        logger.info("getChatRoomMessages for: {}", token)
        val resp = chatRoomServices.getChatMessages(token, id)
        return responseGenerator(resp, Uris.ChatRooms.GET_CHAT_ROOM_MESSAGES) {
            resp.res
        }
    }

    @PostMapping(Uris.ChatRooms.ADD_MESSAGE_TO_CHAT_ROOM)
    fun addChatRoomMessage(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody message: MessageInput,
    ): ResponseEntity<*> {
        logger.info("addChatRoomMessage for: {}", token)
        val resp = chatRoomServices.addMessage(token, id, message.text)
        return responseGenerator(resp, Uris.ChatRooms.ADD_MESSAGE_TO_CHAT_ROOM) {
            resp.res?.toBooleanOut()
        }
    }

    @PutMapping(Uris.ChatRooms.VIEW_MESSAGE_CHAT_ROOM)
    fun viewChatRoomMessage(
        @RequestHeader("Authorization") token: String,
        @PathVariable roomId: String,
        @RequestBody data: ViewMessageInput,
    ): ResponseEntity<*> {
        logger.info("viewChatRoomMessage for: {}", token)
        val resp = chatRoomServices.viewMessage(token, roomId, data.messageId)
        return responseGenerator(resp, Uris.ChatRooms.VIEW_MESSAGE_CHAT_ROOM) {
            resp.res?.toBooleanOut()
        }
    }
}
