package com.example.demo.meanings.domain.model

import com.fasterxml.jackson.databind.ObjectMapper

data class WordInfo(
    val word: String,
    val phonetic: String?,
    val meanings: List<Meaning>,
) {
    fun toJson(): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(this)
    }
}

fun wordInfoFromJson(json: String): WordInfo {
    val objectMapper = ObjectMapper()
    return objectMapper.readValue(json, WordInfo::class.java)
}
