package com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes // ktlint-disable package-name

import android.content.Context

sealed class ClickEvent {
    class Next(val context: Context) : ClickEvent()
    class Previous(val context: Context) : ClickEvent()
}
