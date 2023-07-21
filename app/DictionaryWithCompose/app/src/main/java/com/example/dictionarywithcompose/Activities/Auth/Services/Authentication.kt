package com.example.dictionarywithcompose.Activities.Auth.Services

import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthMedia
import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthResult

interface Authentication {

    suspend fun authenticate(
        authMedia: AuthMedia,
    ): AuthResult

    suspend fun signUp(
        authMedia: AuthMedia,
        callback: () -> Unit,
        onError: () -> Unit,
    ): Boolean
}
