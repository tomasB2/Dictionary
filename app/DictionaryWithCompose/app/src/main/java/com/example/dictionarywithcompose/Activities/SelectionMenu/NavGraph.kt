package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Box // ktlint-disable import-ordering
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraView
// import com.example.dictionarywithcompose.Activities.CameraXRelated.Preview
// import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraXScreen
import com.example.dictionarywithcompose.Activities.SpeechRecognition.DisplayWordMeaning
import com.example.dictionarywithcompose.Activities.SpeechRecognition.SearchScreen
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.Executors

// ktlint-disable package-name

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String,
    permissionState: PermissionState,
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
            CameraScreen(navController = navHostController, permissionState = permissionState )
        }
        composable("settings") {
            SettingsScreen(navController = navHostController)
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(
    permissionState: PermissionState,
) {
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Button(onClick = { permissionState.launchPermissionRequest() }) {
            }
        },
        permissionNotAvailableContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "Camera permission is required to use this feature",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) {
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val outputDirectory = LocalContext.current.filesDir
        fun handleImageCapture(uri: Uri) {
            Log.i("Camera", "Image captured: $uri")
        }

        CameraView(
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = ::handleImageCapture,
            onError = { Log.e("Camera", "View error:", it) },
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(navController: NavHostController, permissionState: PermissionState) {
    CameraPermission(permissionState = permissionState)
}

@Composable
fun ProfileScreen(navController: NavHostController) {
}

@Composable
fun TranslationScreen(navController: NavHostController) {
}

@Composable
fun SearchScreenFull(navController: NavHostController) {
    ThemeConstructor {
        SearchScreen()
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
/**
 *
 */
