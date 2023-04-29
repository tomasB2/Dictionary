package com.example.demo.common.http.utils

import com.example.demo.common.http.outputStructures.toErrorResponseOut
import com.example.demo.common.domain.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <R> responseGenerator(resp: Response<*>, path: String, processOutput: () -> R): ResponseEntity<*> {
    if (resp.e != null) {
        return ResponseEntity(
            resp.e.toErrorResponseOut(path),
            resp.e.cause.toHttpStatus(),
        )
    }
    return ResponseEntity(
        processOutput(),
        HttpStatus.OK,
    )
}
