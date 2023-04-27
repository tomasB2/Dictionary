package com.example.dictionarywithcompose.Activities.Auth // ktlint-disable package-name

import androidx.lifecycle.ViewModel
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthState
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationEvent
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationMode
import com.example.dictionarywithcompose.Activities.Auth.DataType.PasswordRequirement
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {
    val uiState = MutableStateFlow(AuthState())

    fun handleEvent(authenticationEvent: AuthenticationEvent) {
        when (authenticationEvent) {
            is AuthenticationEvent.ToggleAuthenticationMode -> {
                toggleAuthenticationMode()
            }
            is AuthenticationEvent.EmailChanged -> {
                updateEmail(authenticationEvent.emailAddress)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(authenticationEvent.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate()
            }
            is AuthenticationEvent.ErrorDismissed -> {
                dismissError()
            }
        }
    }
    private fun toggleAuthenticationMode() {
        val authenticationMode = uiState.value.authenticationMode
        val newAuthenticationMode = if (
            authenticationMode == AuthenticationMode.SIGN_IN
        ) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        uiState.value = uiState.value.copy(
            authenticationMode = newAuthenticationMode,
        )
    }
    private fun updateEmail(email: String) {
        uiState.value = uiState.value.copy(email = email)
    }

    private fun updatePassword(password: String) {
        val requirements = mutableListOf<PasswordRequirement>()
        if (password.length > 7) {
            requirements.add(PasswordRequirement.EIGHT_CHARACTERS)
        }
        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirement.CAPITAL_LETTER)
        }
        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirement.NUMBER)
        }
        uiState.value = uiState.value.copy(
            password = password,
            passwordRequirements = requirements.toList(),
        )
    }
    private fun authenticate() {
        uiState.value = uiState.value.copy(
            isLoading = true,
        )
        /**
         * TODO: Add authentication logic here
         */
    }
    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null,
        )
    }
}
