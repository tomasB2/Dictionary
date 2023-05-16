package com.example.demo.user

import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.removeBearer
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.user.domain.inputs.LoginInput
import com.example.demo.user.domain.inputs.UserInput
import com.example.demo.user.domain.outpus.toUserOut
import com.example.demo.user.service.UserServiceInterface
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@Suppress("unused")
@RestController
class UserController(
    val userServices: UserServiceInterface,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(Uris.Users.GET_BY_NAME)
    fun findUserHandler(
        @PathVariable name: String,
    ): Any {
        logger.info("findUserHandler for: {}", name)
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
        logger.info("findUsersHandler for: {} {}", limit, offset)
        val resp = userServices.getUsers(limit = limit, offset = offset)
        return responseGenerator(resp, Uris.Users.GET_LIST) {
            resp.res.map { it.toUserOut() }
        }
    }

    @PostMapping(Uris.Users.LOGIN)
    fun loginHandler(
        @RequestBody data: LoginInput,
    ): Any {
        logger.info("loginHandler for: {}, {}", data.name, data.email)
        val resp = userServices.login(data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.LOGIN) {
            resp.res
        }
    }

    @PostMapping(Uris.Users.LOGOUT)
    fun logoutHandler(
        @RequestHeader("Authorization") token: String,
    ): Any {
        logger.info("logoutHandler for: {}", token)
        val resp = userServices.logout(removeBearer(token))
        return responseGenerator(resp, Uris.Users.LOGOUT) {
            resp.res
        }
    }

    @PostMapping(Uris.Users.CREATE)
    fun createUserHandler(
        @RequestBody data: UserInput,
    ): Any {
        logger.info("createUserHandler for: {}", data.name)
        val resp = userServices.create(data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.CREATE) {
            resp.res?.toUserOut()
        }
    }

    @PostMapping(Uris.Users.EDIT)
    fun editUserHandler(
        @RequestHeader("Authorization") token: String,
        @RequestBody data: UserInput,
    ): Any {
        logger.info("editUserHandler for: {}", token)
        val resp = userServices.updateUser(token, data.name, data.email, data.password)
        return responseGenerator(resp, Uris.Users.EDIT) {
            resp.res?.toUserOut()
        }
    }

    @GetMapping(Uris.Users.TOKEN)
    fun checkTokenHandler(
        @RequestHeader("Authorization") token: String,
    ): Any {
        logger.info("checkTokenHandler for: {}", token)
        val resp = userServices.checkToken(removeBearer(token))
        return responseGenerator(resp, Uris.Users.TOKEN) {
            resp.res
        }
    }
}
