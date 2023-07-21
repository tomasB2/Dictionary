package com.example.demo.friends

import com.example.demo.common.http.outputStructures.toBooleanOut
import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.friends.service.FriendsServiceInterface
import com.example.demo.user.domain.outpus.toUserOut
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

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

    @GetMapping(Uris.Requests.GET_REQUESTS)
    fun getRequests(
        @RequestHeader("Authorization") token: String,
    ): Any {
        logger.info("getFriends for: {}", token)
        val resp = friendsService.getFriendRequests(token)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res?.map { it.toUserOut() }
        }
    }

    @PutMapping(Uris.Friends.ADD_FRIEND)
    fun addFriend(
        @RequestHeader("Authorization") token: String,
        @PathVariable name: String,
    ): Any {
        logger.info("addFriend for: {}", name)
        val resp = friendsService.addFriend(token, name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res.toBooleanOut()
        }
    }

    @PutMapping(Uris.Requests.ACC_FRIEND)
    fun accFriendRequest(
        @RequestHeader("Authorization") token: String,
        @PathVariable name: String,
    ): Any {
        logger.info("addFriend for: {}", name)
        val resp = friendsService.acceptFriendRequest(token, name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res.toBooleanOut()
        }
    }

    @PostMapping(Uris.Requests.DEC_FRIEND)
    fun decFriendRequest(
        @RequestHeader("Authorization") token: String,
        @PathVariable name: String,
    ): Any {
        logger.info("addFriend for: {}", name)
        val resp = friendsService.declineFriendRequest(token, name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res.toBooleanOut()
        }
    }

    @PostMapping(Uris.Friends.REMOVE_FRIEND)
    fun removeFriend(
        @RequestHeader("Authorization") token: String,
        @PathVariable name: String,
    ): Any {
        logger.info("removeFriend for: {}", name)
        val resp = friendsService.removeFriend(token, name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res.toBooleanOut()
        }
    }
}
