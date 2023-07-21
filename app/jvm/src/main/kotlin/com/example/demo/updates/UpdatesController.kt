package com.example.demo.updates

import com.example.demo.common.domain.Response
import com.example.demo.common.http.utils.Uris
import com.example.demo.common.repository.implementations.TransactionManagerImp
import com.example.demo.common.utils.authenticate
import com.example.demo.user.domain.User
import com.example.demo.user.service.UserServiceInterface
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping(Uris.Updates.GET_UPDATES)
class UpdatesController(
    private val sseRegister: SseRegister = SseRegister,
    private val userServices: UserServiceInterface,
) {

    @GetMapping(produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamSSE(
        @RequestHeader("Authorization") token: String,
    ): SseEmitter {
        var user: Response<User?>? = null
        TransactionManagerImp.run { transaction ->
            user = authenticate(token, transaction)
        }
        user = userServices.getUserByToken(token) // fazer logica
        val emitter = SseEmitter()
        // Set up the SSE emitter
        //SseRegister.registerSseClient()
        return emitter
    }
} // Here if for updating friends requests, chat rooms and messages with a SseEmitter