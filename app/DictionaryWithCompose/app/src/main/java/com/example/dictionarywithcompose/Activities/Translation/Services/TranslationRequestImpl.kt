package com.example.dictionarywithcompose.Activities.Translation.Services

import android.util.Log
import com.example.dictionarywithcompose.Utils.JsonHelper
import com.example.dictionarywithcompose.Utils.Uris_list
import com.example.dictionarywithcompose.Utils.send
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

data class TranslationInput(
    val from: String?,
    val to: String,
    val text: String,
)
data class Translationout(
    val sourceLanguage: String?,
    val sourceText: String,
    val targetLanguage: String,
    val targetText: String,
)
object TranslationRequestImpl : TranslationRequest {
    override suspend fun getTranslation(client: OkHttpClient, translationInput: TranslationInput): Map<String, String> {
        val mutableMap = mutableMapOf<String, String>()
        Log.v("Meaning", "Requesting meaning")
        val request = Request.Builder()
            .url(Uris_list.TRANSLATION)
            .post(
                JsonHelper.toJson(translationInput)
                    .toRequestBody("application/json".toMediaTypeOrNull()),
            )
            .build()
        Log.v("Meaning", request.body.toString())
        request.send(
            client,
            handler = {
                Log.v("Meaning", it.body.toString())
                if (it.code != 200) {
                    Log.v("Meaning", "Error")
                    return@send
                }
                val a = parseTranslationWithGson(it.body!!.string())
                mutableMap["target"] = a.targetText
                mutableMap["target_language"] = a.targetLanguage
            },
        )
        return mutableMap
    }
}
fun parseTranslationWithGson(json: String): Translationout {
    val gson = Gson()
    return gson.fromJson(json, Translationout::class.java)
}
