package com.example.demo.chatRooms.models

fun String.toListOfUserIds() = this.split(",").map { it.toInt() }
