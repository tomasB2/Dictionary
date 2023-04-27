package com.example.demo.http.outputStructures

import domain.ErrorResponse

data class ErrorResponseOut(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String,
)

fun ErrorResponse.toErrorResponseOut(path: String) =
    ErrorResponseOut(
        timestamp = timestamp.toString(),
        status = this.cause.toHttpStatus().value(),
        error = this.message,
        path = path,
    )
