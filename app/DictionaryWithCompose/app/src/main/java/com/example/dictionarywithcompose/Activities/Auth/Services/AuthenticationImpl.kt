package com.example.dictionarywithcompose.Activities.Auth.Services // ktlint-disable package-name

import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthMedia
import com.example.dictionarywithcompose.Activities.Auth.DataType.WebRelated.AuthResult
import com.example.dictionarywithcompose.Utils.JsonHelper
import com.example.dictionarywithcompose.Utils.Uris_list
import com.example.dictionarywithcompose.Utils.send
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object AuthenticationImpl : Authentication {
    override suspend fun authenticate(authMedia: AuthMedia): AuthResult {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(Uris_list.AUTHENTICATE)
            .post(
                JsonHelper.toJson(authMedia)
                    .toRequestBody("application/json".toMediaTypeOrNull()),
            )
            .build()
        var authResult = AuthResult(null, null)
        request.send(
            okHttpClient,
        ) {
            if (it.isSuccessful) {
                val json = it.body?.string()
                authResult = parseResponseWithGson(json!!)
            }
        }
        return authResult
    }

    override suspend fun signUp(authMedia: AuthMedia, callback: () -> Unit, onError: () -> Unit): Boolean {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(Uris_list.REGISTER)
            .post(
                JsonHelper.toJson(authMedia)
                    .toRequestBody("application/json".toMediaTypeOrNull()),
            )
            .build()
        var isSuccessful = false
        request.send(
            okHttpClient,
        ) {
            if (it.isSuccessful) {
                isSuccessful = true
                callback()
            } else {
                onError()
            }
        }
        return isSuccessful
    }
    private fun parseResponseWithGson(json: String): AuthResult {
        val gson = Gson()
        return gson.fromJson(json, AuthResult::class.java)
    }
}
