package com.example.demo.http.pipeline

import com.example.demo.domain.meanings.model.Meaning
import domain.User
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

private val logger = LoggerFactory.getLogger(UserArgumentResolver::class.java)

@Component
class UserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == User::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException("TODO")
        return getUserFrom(request) ?: throw IllegalStateException("TODO")
    }

    companion object {
        private const val KEY = "UserArgumentResolver"

        fun addUserTo(user: User, request: HttpServletRequest) {
            return request.setAttribute(KEY, user)
        }

        fun getUserFrom(request: HttpServletRequest): User? {
            return request.getAttribute(KEY)?.let {
                logger.info("$it")
                it as? User
            }
        }
    }
}
