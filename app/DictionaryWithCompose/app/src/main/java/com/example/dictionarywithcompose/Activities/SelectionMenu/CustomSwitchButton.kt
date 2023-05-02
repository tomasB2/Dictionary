package com.example.dictionarywithcompose.Activities.SelectionMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.dictionarywithcompose.R
import kotlin.math.roundToInt

// ktlint-disable package-name

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomSwithButton() {
    val width = 80.dp
    val height = 40.dp

    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { height.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
    val isChecked = swipeableState.currentValue == 1
    fun colorOFBackground(): Color {
        if (isChecked) {
            return Color(0xFF0c1445)
        } else {
            return Color(0xFF9FDBE7)
        }
    }
    Box(
        modifier = Modifier
            .width(width).height(height)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal,
            )
            .background(
                color = colorOFBackground(),
                shape = RoundedCornerShape(percent = 50),
            )
            .border(1.dp, Color.Black, RoundedCornerShape(50)),
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(height).border(1.dp, color = colorOFBackground(), RoundedCornerShape(50)),
        ) {
            Image(
                painter = if (isChecked) {
                    painterResource(
                        id = R.drawable.moon,
                    )
                } else {
                    painterResource(
                        id = R.drawable.sun,
                    )
                },
                contentDescription = if (isChecked) {
                    stringResource(id = R.string.moon_image)
                } else {
                    stringResource(
                        id = R.string.sun_image,
                    )
                },

            )
        }
    }
}
