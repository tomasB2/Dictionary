package com.example.dictionarywithcompose.Activities.ThemeRelated // ktlint-disable package-name

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Color.Black,
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFE5E5E5),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB00020),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF),
)
val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = Color.White,
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFF000000),
)

@Composable
fun ThemeConstructor(
    content: @Composable () -> Unit,
) {
    val state = remember {
        mutableStateOf(ThemeState(Theme.Light))
    }
    MaterialTheme(
        colorScheme = if (state.value.theme == Theme.Light) LightColorScheme else DarkColorScheme,
    ) {
        content()
    }
}
