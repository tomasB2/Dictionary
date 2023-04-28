package com.example.dictionarywithcompose.RequestServices // ktlint-disable package-name

interface SearchServices {
    fun getMeaning(word: String): String
    fun getExample(word: String): String
    fun getSynonyms(word: String): String
    fun getAntonyms(word: String): String
    fun getWordOfTheDay(): String
}
