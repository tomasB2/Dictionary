package com.example.dictionarywithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.dictionarywithcompose.Activities.SelectionMenu.MainNavigatorScreen
import com.example.dictionarywithcompose.ui.theme.DarkPurpleBlue
import com.example.dictionarywithcompose.ui.theme.DictionaryWithComposeTheme
import com.example.dictionarywithcompose.ui.theme.OceanBlue
import com.example.dictionarywithcompose.ui.theme.Purple20

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DictionaryWithComposeTheme {
                // A surface container using the 'background' color from the theme

                // AuthComposable()
                MainNavigatorScreen(LocalContext.current)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val colorStops = arrayOf(
        0.0f to Purple20,
        0.35f to OceanBlue,
        1f to DarkPurpleBlue,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colorStops = colorStops)),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DictionaryWithComposeTheme {
        Greeting("Android")
    }
}
