package com.example.demo.oauth2

import com.example.demo.common.http.utils.Uris
import com.example.demo.common.http.utils.responseGenerator
import com.example.demo.user.domain.LogInInfo
import com.example.demo.user.service.UserServices
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2LoginSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val userServices = UserServices()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        logger.info("Google Authentication successful")
        val oAuth2User = authentication.principal as OAuth2User

        // Retrieve user details from the OAuth2User object
        val email = oAuth2User.getAttribute<String>("email")
        val name = oAuth2User.getAttribute<String>("name")
        if (name != null && email != null) {
            var resp: ResponseEntity<*>? = null
            val user = userServices.getUser(name)
            if (!user.isSuccessful) {
                val info = userServices.createWithGoogle(name, email)
                if (!info.isSuccessful) {
                    resp = responseGenerator(info, Uris.Users.LOGIN_GOOGLE) {
                        info.res
                    }
                }
            }
            if (resp == null) {
                val login = userServices.loginWithGoogle(name, email)
                resp = responseGenerator(login, Uris.Users.LOGIN_GOOGLE) {
                    login.res
                }
            }
            response.writer.write(textToJson(resp.body))
            return
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }

    fun textToJson(info: Any?): String {
        val gson = Gson()
        val json = gson.toJson(info)
        return json
    }
}
