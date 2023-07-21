package com.example.dictionarywithcompose.Activities.HomeScreen // ktlint-disable package-name

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionarywithcompose.Activities.HomeScreen.Shapes.TriangleBackShape
import com.example.dictionarywithcompose.Activities.HomeScreen.Shapes.TriangleNextShape
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.Activities.Meaning.DisplayWordMeaning

@Composable
fun LastSearches(
    currentlyDisplayed: MeaningContent,
    isEmpty: Boolean = true,
) {
    if (!isEmpty) {
        DisplayWordMeaning(currentlyDisplayed)
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "No searches yet")
        }
    }
}

@Composable
fun LastSearchesBox(
    BoxContent: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Last Searches:",
                style = TextStyle(
                    fontSize = 45.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.padding(32.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                BoxContent()
            }
        }
    }
}

@Composable
fun ComposeBox(
    currentlyDisplayed: MeaningContent,
    isfirst: Boolean = false,
    islast: Boolean = false,
    onClickNext: () -> Unit = {},
    onClickPrevious: () -> Unit = {},
    isEmpty: (Context) -> Boolean = { true },
) {
    val context = LocalContext.current
    Box() {
        LastSearchesBox {
            LastSearches(
                currentlyDisplayed = currentlyDisplayed,
                isEmpty(context),
            )
        }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Arrows(isfirst = isfirst, islast = islast, onClickPrevious = onClickPrevious, onClickNext = onClickNext)
        }
    }
}

@Composable
fun Arrows(
    isfirst: Boolean,
    islast: Boolean,
    onClickPrevious: () -> Unit,
    onClickNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
    ) {
        if (!isfirst) {
            Box(
                Modifier.size(92.dp)
                    .padding(52.dp, 0.dp, 12.dp, 62.dp),
            ) {
                TriangleBackShape(onClickPrevious)
            }
        }
        if (!islast) {
            Box(Modifier.size(92.dp).padding(12.dp, 0.dp, 52.dp, 62.dp)) {
                TriangleNextShape(onClickNext)
            }
        }
    }
}
