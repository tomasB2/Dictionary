package com.example.dictionarywithcompose.Utils // ktlint-disable package-name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.example.dictionarywithcompose.ui.theme.DarkPurpleBlue
import com.example.dictionarywithcompose.ui.theme.OceanBlue
import com.example.dictionarywithcompose.ui.theme.Purple20

@Composable
fun ChangeBackground(
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val colorStops = arrayOf(
            0.0f to Purple20,
            0.35f to OceanBlue,
            1f to DarkPurpleBlue,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(colorStops = colorStops)),
        ) {
            content.invoke()
        }
    }
}
