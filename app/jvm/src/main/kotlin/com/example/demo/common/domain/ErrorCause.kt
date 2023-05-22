package com.example.demo.common.domain

import org.springframework.http.HttpStatus

enum class ErrorCause {
    USER_NOT_FOUND,
    USER_ALREADY_EXISTS,
    WRONG_PASSWORD,
    EMAIL_ALREADY_EXISTS,
    INVALID_EMAIL,
    EMAIL_NOT_FOUND,
    USER_UNAUTHORIZED,
    USER_BAD_REQUEST,
    TOKEN_NOT_FOUND,
    INVALID_TOKEN,
    SERVER_ERROR,
    WORD_NOT_FOUND,
    INVALID_LANGUAGE,
    DATABASE_ERROR,
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    TOKEN_NOT_VALID,
    NO_USER_REQUEST,
    USER_NOT_ADDED,
    USER_ALREADY_ADDED,
    USER_ALREADY_REQUESTED,
    ;

    fun toHttpStatus() = when (this) {
        USER_NOT_FOUND -> HttpStatus.NOT_FOUND
        USER_ALREADY_EXISTS -> HttpStatus.BAD_REQUEST
        WRONG_PASSWORD -> HttpStatus.UNAUTHORIZED
        EMAIL_ALREADY_EXISTS -> HttpStatus.BAD_REQUEST
        INVALID_EMAIL -> HttpStatus.BAD_REQUEST
        EMAIL_NOT_FOUND -> HttpStatus.NOT_FOUND
        USER_UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
        USER_BAD_REQUEST -> HttpStatus.BAD_REQUEST
        TOKEN_NOT_FOUND -> HttpStatus.NOT_FOUND
        INVALID_TOKEN -> HttpStatus.UNAUTHORIZED
        SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        WORD_NOT_FOUND -> HttpStatus.NOT_FOUND
        INVALID_LANGUAGE -> HttpStatus.BAD_REQUEST
        DATABASE_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
        USERNAME_TAKEN -> HttpStatus.BAD_REQUEST
        EMAIL_TAKEN -> HttpStatus.BAD_REQUEST
        TOKEN_NOT_VALID -> HttpStatus.UNAUTHORIZED
        NO_USER_REQUEST -> HttpStatus.NOT_FOUND
        USER_NOT_ADDED -> HttpStatus.BAD_REQUEST
        USER_ALREADY_ADDED -> HttpStatus.BAD_REQUEST
        USER_ALREADY_REQUESTED -> HttpStatus.BAD_REQUEST
    }
}
