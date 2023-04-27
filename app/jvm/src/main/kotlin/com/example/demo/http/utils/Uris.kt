package com.example.demo.http.utils

object Uris {
    const val ROOT = "/api"

    object Users {
        private const val prefix = "$ROOT/users"
        const val CREATE = "$prefix/create"
        const val EDIT = "$prefix/edit"
        const val LOGIN = "$prefix/login"
        const val LOGOUT = "$prefix/logout"
        const val TOKEN = "$prefix/token"
        const val GET_BY_NAME = "$prefix/{name}"
        const val GET_LIST = prefix
    }

    object PreviousSearches {
        private const val prefix = "$ROOT/searches"
        const val GET_PREVIOUS_SEARCHES = prefix
        const val DELETE_PREVIOUS_SEARCHES_BY_ID = "$prefix/{word}"
        const val DELETE_PREVIOUS_SEARCHES_BY_USER = prefix
    }

    object Translation {
        private const val prefix = "$ROOT/translation"
        const val GET_TRANSLATION = prefix
    }

    object Word {
        private const val prefix = "$ROOT/words"
        const val GET_MEANING = prefix
    }

    fun removeInputBraces(s: String): String {
        return s.replace("{", "").replace("}", "")
    }
}