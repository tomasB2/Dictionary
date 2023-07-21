package com.example.dictionarywithcompose.Utils // ktlint-disable package-name

import com.google.gson.Gson
object JsonHelper {
    val gson = Gson()
    fun <T : Any> toJson(obj: T): String {
        return gson.toJson(obj)
    }
    fun <T : Any> fromJson(json: String, obj: Class<T>): T {
        return gson.fromJson(json, obj)
    }
}
