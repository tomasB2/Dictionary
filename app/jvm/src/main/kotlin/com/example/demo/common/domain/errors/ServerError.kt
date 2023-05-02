package com.example.demo.common.domain.errors

class ServerError(
    override val message: String,
    override val cause: Throwable?,
) : Exception()
