package com.example.demo.common.http.outputStructures

data class BooleanOut(
    val res: Boolean,
)

fun Boolean.toBooleanOut() = BooleanOut(
    res = this,
)
