package com.example.dictionarywithcompose.Activities.ThemeRelated // ktlint-disable package-name

data class ThemeState(val theme: Theme) {
    fun toggleTheme() = copy(theme = if (theme == Theme.Light) Theme.Dark else Theme.Light)
}

enum class Theme {
    Light, Dark
}
