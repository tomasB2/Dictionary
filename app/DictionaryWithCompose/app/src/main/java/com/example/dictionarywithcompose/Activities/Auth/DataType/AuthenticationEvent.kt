package com.example.dictionarywithcompose.Activities.Auth.DataType // ktlint-disable package-name

sealed class AuthenticationEvent {

    object ToggleAuthenticationMode : AuthenticationEvent()

    class EmailChanged(val emailAddress: String) : AuthenticationEvent()

    class PasswordChanged(val password: String) : AuthenticationEvent()
    object ErrorDismissed : AuthenticationEvent()

    object Authenticate : AuthenticationEvent()
}
