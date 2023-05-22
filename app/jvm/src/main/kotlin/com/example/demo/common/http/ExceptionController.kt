package com.example.demo.common.http

import com.example.demo.common.domain.ErrorCause
import com.example.demo.common.domain.ErrorResponse
import com.example.demo.common.domain.errors.ServerError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private data class ExceptionResponse(val error: String, val message: String)

@Suppress("unused")
@ControllerAdvice
class ExceptionController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(error = e.message, cause = ErrorCause.USER_BAD_REQUEST), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ServerError::class)
    fun handleServerError(e: ServerError, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(ErrorResponse(error = e.message, cause = ErrorCause.SERVER_ERROR), HttpStatus.BAD_REQUEST)
    }
}
