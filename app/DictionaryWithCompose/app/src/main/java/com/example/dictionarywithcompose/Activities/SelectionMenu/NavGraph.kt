package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dictionarywithcompose.Activities.SpeechRecognition.SearchScreen
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper

// ktlint-disable package-name

@Composable
fun navGraph(
    navHostController: NavHostController,
    startDestination: String,
    // fixing the bug
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("home") {
            HomeScreen(navController = navHostController)
        }
        composable("search") {
            SearchScreenFull(navController = navHostController)
        }
        composable("profile") {
            ProfileScreen(navController = navHostController)
        }
        composable("translation") {
            TranslationScreen(navController = navHostController)
        }
        composable("camera") {
            CameraScreen(navController = navHostController)
        }
        composable("settings") {
            SettingsScreen(navController = navHostController)
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
}

@Composable
fun CameraScreen(navController: NavHostController) {
}

@Composable
fun ProfileScreen(navController: NavHostController) {
}

@Composable
fun TranslationScreen(navController: NavHostController) {
}

@Composable
fun SearchScreenFull(navController: NavHostController) {
    MaterialTheme {
        SearchScreen(navController = navController)

    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
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
                    Text(text = search.toString())
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
