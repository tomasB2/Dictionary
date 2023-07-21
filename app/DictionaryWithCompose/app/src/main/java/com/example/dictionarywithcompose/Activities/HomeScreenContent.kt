package com.example.dictionarywithcompose.Activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen() {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
    ) {
        androidx.compose.material.Text(text = "Home Screen")
    }
}
