package com.example.dictionarywithcompose.Activities.Auth // ktlint-disable package-name

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthState
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationEvent
import com.example.dictionarywithcompose.Activities.Auth.DataType.AuthenticationMode
import com.example.dictionarywithcompose.Activities.Auth.DataType.PasswordRequirement
import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthMedia
import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthResult
import com.example.dictionarywithcompose.Activities.Auth.Services.AuthenticationImpl
import com.example.dictionarywithcompose.Activities.SelectionMenu.NavigationDrawer
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
                changeToAuthenticate(authenticationEvent.context)
            }
            is AuthenticationEvent.ErrorDismissed -> {
                dismissError()
            }
            is AuthenticationEvent.Loading -> {
                authenticate()
            }
            is AuthenticationEvent.UsernameChanged -> {
                updateUsername(authenticationEvent.username)
            }
        }
    }

    private fun updateUsername(username: String) {
        uiState.value = uiState.value.copy(username = username)
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
        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                isLoading = true,
            )
        }
    }
    private fun changeToAuthenticate(context: Context) {
        viewModelScope.launch {
            goToHome(context)
        }
    }
    private suspend fun goToHome(context: Context) {
        val dbHelper = MyDatabaseHelper(context)
        if (uiState.value.authenticationMode == AuthenticationMode.SIGN_UP) {
            signUpUser(
                uiState.value.username ?: "",
                uiState.value.email ?: "",
                uiState.value.password ?: "",
            )
            Log.v("user", "sign up")
            return
        }
        val loginUser = loginUser(
            uiState.value.username ?: "",
            uiState.value.password ?: "",
        )
        if (loginUser.name == null || loginUser.token == null) {
            uiState.value = uiState.value.copy(
                isLoading = false,
                authenticationMode = AuthenticationMode.SIGN_IN,
            )
            return
        } else {
            Intent(
                context,
                NavigationDrawer::class.java,
            ).also {
                val user = dbHelper.getUserLogin()
                Log.v("user", user.toString())
                it.putExtra("username", uiState.value.username)
                startActivity(context, it, null)
            }
        }
    }

    private fun dismissError() {
        uiState.value = uiState.value.copy(
            error = null,
        )
    }

    private suspend fun loginUser(username: String, password: String): AuthResult {
        val authMedia = AuthMedia(name = username, password = password)
        return AuthenticationImpl.authenticate(authMedia)
    }

    private suspend fun signUpUser(username: String, email: String, password: String) {
        val authMedia = AuthMedia(name = username, email = email, password = password)
        AuthenticationImpl.signUp(
            authMedia,
            callback = {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    authenticationMode = AuthenticationMode.SIGN_IN,
                )
            },
            onError = {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    username = "",
                    email = "",
                    password = "",
                )
            },
        )
    }
}
