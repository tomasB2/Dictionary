package com.example.dictionarywithcompose.Utils

import android.content.Context

fun getTextFromResourceId(context: Context, resourceId: Int): String {
    return context.resources.getString(resourceId)
}
