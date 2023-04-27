package com.example.demo.domain.errors

class ServerError(
    override val message: String,
    override val cause: Throwable?,
) : Exception()
