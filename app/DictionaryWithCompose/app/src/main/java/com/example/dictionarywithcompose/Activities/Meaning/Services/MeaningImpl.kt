package com.example.dictionarywithcompose.Activities.Meaning.Services // ktlint-disable package-name
import android.util.Log
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.Utils.JsonHelper
import com.example.dictionarywithcompose.Utils.Uris_list
import com.example.dictionarywithcompose.Utils.send
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

// ktlint-disable package-name
data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>,
)

data class Meaning(
    @SerializedName("partOfSpeech")
    val partOfSpeech: String,
    val definitions: List<Definition>,
)

data class WordInfo(
    val lang: String,
    val word: String,
    val phonetic: String,
    val meanings: List<Meaning>,
)

data class ApiResponse(
    @SerializedName("wordInfo")
    val wordInfo: WordInfo,
    val placedInUserHistory: Boolean,
    val reason: String?,
)

fun parseResponseWithGson(json: String): WordInfo {
    val gson = Gson()
    val apiResponse = gson.fromJson(json, ApiResponse::class.java)
    return apiResponse.wordInfo
}
object MeaningImpl : MeaningServices {
    data class MeaningRequest(
        val lang: String,
        val word: String,
        val userToken: String? = null,
    )
    override suspend fun getWord(client: OkHttpClient, word: String, lang: String): List<MeaningContent> {
        var mutableList: List<MeaningContent> = mutableListOf()
        Log.v("Meaning", "Requesting meaning")
        val language = toString(lang.map { it.lowercaseChar() })
        val wordUnder = toString(word.map { it.lowercaseChar() })
        val request = Request.Builder()
            .url(Uris_list.MEANING)
            .post(
                JsonHelper.toJson(MeaningRequest(language, wordUnder))
                    .toRequestBody("application/json".toMediaTypeOrNull()),
            )
            .build()
        request.send(
            client,
            handler = { it ->
                Log.v("Meaning", it.body.toString())
                if (it.code != 200) {
                    Log.v("Meaning", "No meaning found")
                    mutableList = listOf(
                        MeaningContent(
                            word,
                            "No meaning found",
                            "no example found",
                        ),
                    )
                    return@send
                }

                val a = parseResponseWithGson(it.body!!.string())
                mutableList = a.meanings.buildList(word)
            },
        )
        return mutableList
    }
}

private fun sort(list: List<Meaning>): Pair<Int, Int> {
    var index = 0
    var defIndex = 0
    var max = list[0].definitions[0].definition.length
    for (i in list.indices) {
        for (j in list[i].definitions.indices) {
            if (list[i].definitions[j].definition.length > max) {
                max = list[i].definitions[j].definition.length
                index = i
                defIndex = j
            }
        }
    }
    return Pair(index, defIndex)
}
private fun List<Meaning>.buildList(word: String): List<MeaningContent> {
    val list = mutableListOf<MeaningContent>()
    this.forEach {
            meaning ->
        meaning.definitions.forEach {
            list.add(
                MeaningContent(
                    word,
                    it.definition,
                    it.example ?: "No example found",
                ),
            )
        }
    }
    return list
}
private fun toString(list: List<Char>): String {
    val stringBuilder = StringBuilder()
    for (i in list) {
        stringBuilder.append(i)
    }
    return stringBuilder.toString()
}
