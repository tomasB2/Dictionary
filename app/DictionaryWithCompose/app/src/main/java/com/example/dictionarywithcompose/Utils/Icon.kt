package com.example.dictionarywithcompose.Utils

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BarIcons(
    changeState: () -> Unit,
    list: Map<String, ImageVector> = mapOf(),
) {
    Row() {
        list.forEach { (t, u) ->
            IconButton(
                onClick = changeState,
                content = { Icon(imageVector = u, contentDescription = t) },
            )
        }
    }
}
