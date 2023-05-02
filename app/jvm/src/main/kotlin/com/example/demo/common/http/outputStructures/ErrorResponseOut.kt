package com.example.demo.common.http.outputStructures

import com.example.demo.common.domain.ErrorResponse

data class ErrorResponseOut(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String,
)

fun ErrorResponse.toErrorResponseOut(path: String) =
    com.example.demo.common.http.outputStructures.ErrorResponseOut(
        timestamp = timestamp.toString(),
        status = this.cause.toHttpStatus().value(),
        error = this.message ?: "Unexpected Error",
        path = path,
    )
