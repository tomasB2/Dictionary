package com.example.demo.common.repository.utils

object PreviousSearchesQueris {

    const val ADD_PREVIOUS_SEARCH = "INSERT INTO searches (user_id, search_key, searches_json) VALUES (?, ?, ?)"

    const val WORD_EXIST_IN_USER = "SELECT searches_json FROM searches WHERE user_id = ? AND search_key = ?"

    const val GET_PREVIOUS_SEARCH = "SELECT searches_json FROM searches WHERE search_key = ?"

    const val GET_PREVIOUS_SEARCHES = "SELECT searches_json FROM searches WHERE user_id = ?"

    const val DELETE_PREVIOUS_SEARCHES = "DELETE FROM searches WHERE user_id = ?"

    const val DELETE_PREVIOUS_SEARCH = "DELETE FROM searches WHERE user_id = ? AND search_key = ?"
}
