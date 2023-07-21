package com.example.demo.common.http.utils

import com.example.demo.common.domain.Response
import com.example.demo.common.http.outputStructures.toErrorResponseOut
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <R> responseGenerator(resp: Response<*>, path: String, processOutput: () -> R): ResponseEntity<*> {
    return if (resp.e != null) {
        ResponseEntity(
            resp.e.toErrorResponseOut(path),
            resp.e.cause.toHttpStatus(),
        )
    } else {
        ResponseEntity(
            processOutput(),
            HttpStatus.OK,
        )
    }
}
