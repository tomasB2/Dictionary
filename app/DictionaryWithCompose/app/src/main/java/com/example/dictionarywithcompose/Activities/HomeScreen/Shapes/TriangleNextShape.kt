package com.example.dictionarywithcompose.Activities.HomeScreen.Shapes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun TriangleNextShape(onPreviousClick: () -> Unit) {
    Spacer(
        modifier = Modifier
            .drawWithCache {
                val path = Path()
                path.moveTo(size.width, size.height / 2f)
                path.lineTo(0f, 0f)
                path.lineTo(0f, size.height)
                path.close()
                onDrawBehind {
                    drawPath(path, Color.Black, style = Stroke(width = 10f))
                    drawPath(path, Color.Black)
                }
            }
            .fillMaxSize().clickable(onClick = onPreviousClick),
    )
}

@Composable
fun TriangleBackShape(onPreviousClick: () -> Unit) {
    Spacer(
        modifier = Modifier
            .drawWithCache {
                val path = Path()
                path.moveTo(0f, size.height / 2f)
                path.lineTo(size.width, 0f)
                path.lineTo(size.width, size.height)
                path.close()
                onDrawBehind {
                    drawPath(path, Color.Black, style = Stroke(width = 10f))
                    drawPath(path, Color.Black)
                }
            }
            .fillMaxSize().clickable(onClick = onPreviousClick),
    )
}
