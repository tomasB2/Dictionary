package com.example.demo.common.domain

data class Response<T>(
    val res: T,
    val e: ErrorResponse?,
) {
    init {
        if (e != null) {
            require(res == null)
        }
        if (res != null) {
            require(e == null)
        }
    }

    val isSuccessful: Boolean
        get() = e == null
}
