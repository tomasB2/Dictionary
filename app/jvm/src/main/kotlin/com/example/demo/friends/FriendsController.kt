package com.example.demo.friends

import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.friends.service.FriendsServiceInterface
import com.example.demo.user.domain.outpus.toUserOut
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class FriendsController(
    val friendsService: FriendsServiceInterface,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(Uris.Friends.GET_FRIENDS)
    fun getFriends(
        @PathVariable name: String,
    ): Any {
        logger.info("getFriends for: {}", name)
        val resp = friendsService.getFriends(name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res?.map { it.toUserOut() }
        }
    }
}
