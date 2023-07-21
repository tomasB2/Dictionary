package com.example.demo.user.domain

enum class Types {
    NORMAL, OAUTH;
    companion object {
        fun Types.toBoolean(): Boolean {
            return when (this) {
                NORMAL -> false
                OAUTH -> true
            }
        }

        fun fromBoolean(value: Boolean): Types {
            return when (value) {
                false -> NORMAL
                true -> OAUTH
            }
        }
    }
}
