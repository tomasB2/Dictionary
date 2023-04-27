package domain

import java.sql.Timestamp

data class ErrorResponse(
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis()),
    val message: String,
    val cause: ErrorCause,
)
