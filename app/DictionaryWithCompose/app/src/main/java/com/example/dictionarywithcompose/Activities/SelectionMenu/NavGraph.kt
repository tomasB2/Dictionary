package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import androidx.compose.foundation.layout.Box // ktlint-disable import-ordering
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraView
import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraViewModel
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.CameraEvent
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.WordState
// import com.example.dictionarywithcompose.Activities.CameraXRelated.Preview
// import com.example.dictionarywithcompose.Activities.CameraXRelated.CameraXScreen
import com.example.dictionarywithcompose.Activities.SpeechRecognition.DisplayWordMeaning
import com.example.dictionarywithcompose.Activities.SpeechRecognition.SearchScreen
import com.example.dictionarywithcompose.Activities.ThemeRelated.ThemeConstructor
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import java.util.concurrent.Executors
import kotlin.reflect.KFunction1

// ktlint-disable package-name

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String,
    permissionState: MultiplePermissionsState,
    // fixing the bug
) {
    val viewModel = CameraViewModel()

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
        composable("camera") {
            // CameraScreen composable function responsible for displaying the camera screen
            // the state is handled by the CameraViewModel
            CameraScreen(
                navController = navHostController,
                permissionState = permissionState,
                state = viewModel.state.collectAsState().value,
                changeState = viewModel::handleEvent,
                uptadeText = viewModel::updateText,
            )
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
    permissionState: MultiplePermissionsState,
    viewModelState: WordState,
    changeState: KFunction1<CameraEvent, Unit>,
    uptadeText: (String) -> Unit,
) {
    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
            }
        },
        permissionsNotAvailableContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "Camera permission is required to use this feature",
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    }
                }
            }
        },
    ) {
        val cameraExecutor = Executors.newSingleThreadExecutor()
        val outputDirectory = LocalContext.current.filesDir

        CameraView(
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            viewModelState = viewModelState,
            changeState = changeState,
            uptadeText = uptadeText,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    navController: NavHostController,
    permissionState: MultiplePermissionsState,
    state: WordState,
    changeState: KFunction1<CameraEvent, Unit>,
    uptadeText: (String) -> Unit,
) {
    val databaseHelper = MyDatabaseHelper(LocalContext.current)
    // val imageSeach = databaseHelper.getLastImageResult()

    CameraPermission(
        permissionState = permissionState,
        viewModelState = state, // viewModel.state.collectAsState().value,
        changeState = changeState, // viewModel::handleEvent,
        uptadeText = uptadeText,
    )
}

@Composable
fun ProfileScreen(navController: NavHostController) {
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
