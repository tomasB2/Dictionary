package com.example.dictionarywithcompose.Activities.Translation.Services

import okhttp3.OkHttpClient

interface TranslationRequest {
    suspend fun getTranslation(client: OkHttpClient, translationInput: TranslationInput): Map<String, String>
}
