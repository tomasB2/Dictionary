package com.example.demo.http.utils // ktlint-disable filename

fun removeBearer(token: String) = token.split(" ")[1]
