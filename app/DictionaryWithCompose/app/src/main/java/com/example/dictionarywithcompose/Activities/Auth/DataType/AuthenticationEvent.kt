package com.example.dictionarywithcompose.Activities.Auth.DataType

import android.content.Context

// ktlint-disable package-name

sealed class AuthenticationEvent {

    object ToggleAuthenticationMode : AuthenticationEvent()

    class EmailChanged(val emailAddress: String) : AuthenticationEvent()
    class UsernameChanged(val username: String) : AuthenticationEvent()
    class PasswordChanged(val password: String) : AuthenticationEvent()
    object ErrorDismissed : AuthenticationEvent()
    object Loading : AuthenticationEvent()
    class Authenticate(val context: Context) : AuthenticationEvent()
}
