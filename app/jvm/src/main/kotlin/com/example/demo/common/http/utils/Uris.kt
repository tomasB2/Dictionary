package com.example.demo.common.http.utils

object Uris {
    const val ROOT = "/api"

    object Users {
        private const val prefix = "$ROOT/users"
        const val CREATE = "$prefix/create"
        const val EDIT = "$prefix/edit"
        const val LOGIN = "$prefix/login"
        const val LOGIN_GOOGLE = "$prefix/login/google"
        const val LOGIN_GOOGLE_CALLBACK = "$prefix/login/google/callback"
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
        const val GET_VALID_LANGUAGES = prefix
    }

    object Friends {
        private const val prefix = "$ROOT/friends"
        const val GET_FRIENDS = "$prefix/{name}"
        const val ADD_FRIEND = "$prefix/{name}"
        const val REMOVE_FRIEND = "$prefix/{name}"
    }
    object Requests {
        private const val prefix = "$ROOT/requests"
        const val GET_REQUESTS = prefix
        const val ACC_FRIEND = "$prefix/{name}"
        const val DEC_FRIEND = "$prefix/{name}"
    }

    object ChatRooms {
        private const val prefix = "$ROOT/chatrooms"
        const val GET_CHAT_ROOM = prefix
        const val UPDATE_CHAT_ROOM = prefix
        const val GET_CHAT_ROOMS = "$prefix/{id}"
        const val ADD_USER_TO_CHAT_ROOM = "$prefix/{id}"
        const val REMOVE_USER_FROM_CHAT_ROOM = "$prefix/{id}"
        const val CREATE_CHAT_ROOM = prefix
        const val ADD_MESSAGE_TO_CHAT_ROOM = "$prefix/message"
        const val VIEW_MESSAGE_CHAT_ROOM = "$prefix/message/{id}"
        const val GET_CHAT_ROOM_MESSAGES = "$prefix/message/{id}"
        const val CONTINUOUSLY_GET_CHAT_ROOM_MESSAGES = "$prefix/message/{id}/continuous"
    }

    object Updates {
        private const val prefix = "$ROOT/updates"
        const val GET_UPDATES = prefix
    }

    fun removeInputBraces(s: String): String {
        return s.replace("{", "").replace("}", "")
    }
}
