package com.example.demo.updates

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

object SseRegister {

    private val sseClients: HashMap<Int, SseEmitter> = hashMapOf()

    fun registerSseClient(userId: Int): SseEmitter {
        val sseEmitter = SseEmitter()
        sseClients[userId] = sseEmitter
        return sseEmitter
    }

    fun removeSseClient(userId: Int) {
        sseClients[userId]?.complete()
        sseClients.remove(userId)
    }

    fun sendSseUpdate(userId: Int, obj: JvmType.Object) {
        sseClients[userId]?.send(obj)
    }
}
