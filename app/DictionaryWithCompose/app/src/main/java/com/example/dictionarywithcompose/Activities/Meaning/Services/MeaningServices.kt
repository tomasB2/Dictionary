package com.example.dictionarywithcompose.Activities.Meaning.Services

import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import okhttp3.OkHttpClient

interface MeaningServices {
    suspend fun getWord(client: OkHttpClient, word: String, lang: String): List<MeaningContent>
}
