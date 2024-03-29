package com.example.demo.common.domain

import java.sql.Timestamp

data class ErrorResponse(
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
    val error: String?,
    val cause: ErrorCause,
)
