package com.example.dictionarywithcompose.Activities.SelectionMenu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking

class SelectionMenuViewModel : ViewModel() {

    suspend fun hasPermissionForCamera(
        function: suspend () -> Boolean,
    ) = runBlocking {
        function()
    }
}
