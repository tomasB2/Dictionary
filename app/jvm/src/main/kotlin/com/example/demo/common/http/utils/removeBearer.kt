package com.example.demo.common.http.utils // ktlint-disable filename

fun removeBearer(token: String) = token.split(" ")[1]
