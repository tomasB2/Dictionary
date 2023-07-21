package com.example.dictionarywithcompose.Utils

import android.content.Context
import android.content.Intent

fun startThisActivity(context: Context, klass: Class<*>, flags: Boolean = false) {
    val intent = Intent(context, klass::class.java)
    if (flags) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    context.startActivity(intent)
}
