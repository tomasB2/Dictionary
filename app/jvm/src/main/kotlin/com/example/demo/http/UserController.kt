package com.example.demo.http

import com.example.demo.Services.UserServiceInterface
import com.example.demo.http.inputStructures.LoginInput
import com.example.demo.http.inputStructures.UserInput
import com.example.demo.http.outputStructures.toUserOut
import com.example.demo.http.utils.Uris
import com.example.demo.http.utils.removeBearer
import com.example.demo.http.utils.responseGenerator
import domain.User
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@Suppress("unused")
@RestController
class UserController(
    val userServices: UserServiceInterface,
) {
    @GetMapping(Uris.Users.GET_BY_NAME)
    fun findUserHandler(
        @PathVariable name: String,
    ): Any {
        val resp = userServices.getUser(name)
        return responseGenerator(resp, Uris.removeInputBraces(Uris.Users.GET_BY_NAME)) {
            resp.res?.toUserOut()
        }
    }

    @GetMapping(Uris.Users.GET_LIST)
    fun findUsersHandler(
        @RequestParam limit: Int,
        @RequestParam offset: Int,
    ): Any {
        val resp = userServices.getUsers(limit = limit, offset = offset)
        return responseGenerator(resp, Uris.Users.GET_LIST) {
            resp.res.map { it.toUserOut() }
        }
    }

    @PostMapping(Uris.Users.LOGIN)
    fun loginHandler(
        @RequestBody data: LoginInput,
    ): Any {
        val resp = userServices.login(data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.LOGIN) {
            resp.res
        }
    }

    @PostMapping(Uris.Users.LOGOUT)
    fun logoutHandler(
        @RequestHeader("Authorization") token: String,
    ): Any {
        val resp = userServices.logout(removeBearer(token))
        return responseGenerator(resp, Uris.Users.LOGOUT) {
            resp.res
        }
    }

    @PostMapping(Uris.Users.CREATE)
    fun createUserHandler(
        @RequestBody data: UserInput,
    ): Any {
        val resp = userServices.create(data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.CREATE) {
            resp.res.toUserOut()
        }
    }

    @PostMapping(Uris.Users.EDIT)
    fun editUserHandler(
        user: User,
        @RequestBody data: UserInput,
    ): Any {
        val resp = userServices.updateUser(user.name, data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.EDIT) {
            resp.res.toUserOut()
        }
    }

    @GetMapping(Uris.Users.TOKEN)
    fun checkTokenHandler(
        @RequestHeader("Authorization") token: String,
    ): Any {
        val resp = userServices.checkToken(removeBearer(token))
        return responseGenerator(resp, Uris.Users.TOKEN) {
            resp.res
        }
    }
}
