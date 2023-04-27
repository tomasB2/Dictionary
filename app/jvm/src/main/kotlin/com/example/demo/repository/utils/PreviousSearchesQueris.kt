package com.example.demo.repository.utils

object PreviousSearchesQueris {

    const val ADD_PREVIOUS_SEARCH = "INSERT INTO searches (user_name, search_key, searches_json) VALUES (?, ?, ?)"

    const val GET_PREVIOUS_SEARCHES = "SELECT word FROM searches WHERE user_name = ?"

    const val DELETE_PREVIOUS_SEARCHES = "DELETE FROM searches WHERE user_name = ?"

    const val DELETE_PREVIOUS_SEARCH = "DELETE FROM searches WHERE user_name = ? AND search_key = ?"
}
