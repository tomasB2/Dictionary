package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import androidx.compose.foundation.layout.Box // ktlint-disable import-ordering
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraXScreen
// import com.example.dictionarywithcompose.Activities.CameraXRelated.Preview
// import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraXScreen
import com.example.dictionarywithcompose.Activities.SpeechRecognition.DisplayWordMeaning
import com.example.dictionarywithcompose.Activities.SpeechRecognition.SearchScreen
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper

// ktlint-disable package-name

@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String,
    buttonToAdd: @Composable () -> Unit = {},
    // fixing the bug
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("home") {
            HomeScreen(navController = navHostController, buttonToAdd = buttonToAdd)
        }
        composable("search") {
            SearchScreenFull(navController = navHostController, buttonToAdd = buttonToAdd)
        }
        composable("profile") {
            ProfileScreen(navController = navHostController, buttonToAdd = buttonToAdd)
        }
        composable("translation") {
            TranslationScreen(navController = navHostController, buttonToAdd = buttonToAdd)
        }
        composable("camera") {
            CameraScreen(navController = navHostController, buttonToAdd = buttonToAdd)
        }
        composable("settings") {
            SettingsScreen(navController = navHostController, buttonToAdd = buttonToAdd)
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
}

@Composable
fun CameraScreen(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
    CameraXScreen()
}

@Composable
fun ProfileScreen(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
}

@Composable
fun TranslationScreen(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
}

@Composable
fun SearchScreenFull(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
    ThemeConstructor {
        SearchScreen(buttonToAdd = buttonToAdd)
    }
}

@Composable
fun HomeScreen(navController: NavHostController, buttonToAdd: @Composable () -> Unit = {}) {
    LastSearches()
}

@Composable
fun LastSearches() {
    val databaseHelper = MyDatabaseHelper(LocalContext.current)
    val searches = databaseHelper.getAllStrings()

    if (searches.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(searches.size) { search ->
                    DisplayWordMeaning(searches[search])
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "No searches yet")
        }
    }
}
