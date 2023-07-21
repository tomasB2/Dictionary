package com.example.demo.oauth2

import com.example.demo.common.http.utils.Uris
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers(Uris.Users.LOGIN_GOOGLE).authenticated()
            .and()
            .oauth2Login()
            .defaultSuccessUrl(Uris.Users.LOGIN_GOOGLE_CALLBACK)
            .userInfoEndpoint()
            .userService(oAuth2UserService())
            .and()
            .successHandler(oAuth2LoginSuccessHandler())
            .and()
            .csrf().disable()
    }

    @Bean
    fun oAuth2UserService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return DefaultOAuth2UserService()
    }

    @Bean
    fun oAuth2LoginSuccessHandler(): AuthenticationSuccessHandler {
        return OAuth2LoginSuccessHandler()
    }
}
