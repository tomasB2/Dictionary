package com.example.demo.oauth2

import com.example.demo.common.http.utils.Uris
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AuthController {

    @GetMapping(Uris.Users.LOGIN_GOOGLE)
    fun login(): String {
        return "redirect:/oauth2/authorization/google"
    }

    @GetMapping(Uris.Users.LOGIN_GOOGLE_CALLBACK)
    fun loginCallback() {
        return
    }
}
