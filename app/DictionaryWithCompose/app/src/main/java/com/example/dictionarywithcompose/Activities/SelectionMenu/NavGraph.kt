package com.example.dictionarywithcompose.Activities.SelectionMenu // ktlint-disable package-name

import android.content.Context
import androidx.compose.foundation.layout.Box // ktlint-disable import-ordering
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dictionarywithcompose.Activities.Common.Camera.TakingPicture
import com.example.dictionarywithcompose.Activities.HomeScreen.ComposeBox
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.ClickEvent
import com.example.dictionarywithcompose.Activities.HomeScreen.DataTypes.HomeState
import com.example.dictionarywithcompose.Activities.HomeScreen.HomeScreenViewModel
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.CameraEvent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningState
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState
import com.example.dictionarywithcompose.Activities.Meaning.MeaningComposable
import com.example.dictionarywithcompose.Activities.Meaning.MeaningViewModel
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationContents
import com.example.dictionarywithcompose.Activities.Translation.DataTypes.TranslationState
import com.example.dictionarywithcompose.Activities.Translation.TranslationComposable
import com.example.dictionarywithcompose.Activities.Translation.TranslationViewModel
import com.example.dictionarywithcompose.SqlLiteDb.MyDatabaseHelper
import com.example.dictionarywithcompose.TakingPicture.PictureTakerImpl
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController,
    startDestination: String,
    permissionState: MultiplePermissionsState,
) {
    val context = LocalContext.current
    val viewModel = MeaningViewModel()
    val viewModelTrans = TranslationViewModel()
    val homeScreenViewModel = HomeScreenViewModel()
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("Home") {
            HomeScreen(
                currentlyDisplayed = homeScreenViewModel.state.collectAsState().value,
                onClickNext = { homeScreenViewModel.changeOnClick(ClickEvent.Next(context)) },
                onClickPrevious = { homeScreenViewModel.changeOnClick(ClickEvent.Previous(context)) },
                isEmpty = homeScreenViewModel::isEmpty,
            )
        }
        composable("Dictionary") {
            MeaningScreen(
                permissionState = permissionState,
                displayWord = viewModel.meaningState.collectAsState().value,
                uiState = viewModel.uiState.collectAsState().value,
                availableLanguages = viewModel.LANGUAGES_AVAILABLE,
                updateText = viewModel::updateDisplayWord,
                handleUiState = viewModel::handleUiState,
                attemptToSearchForNewWord = viewModel::attemptToSearchForNewWord,
                changeState = viewModel::handleCameraResult,
                changeLanguage = viewModel::changeLanguage,
                clickEvent = viewModel::changeIndex,
                isFirst = viewModel.listOfMeaning.collectAsState().value.isFirst,
                isLast = viewModel.listOfMeaning.collectAsState().value.isLast,
            )
        }
        composable("Profile") {
            ProfileScreen(navController = navHostController)
        }
        composable("Translation") {
            SearchScreenFull(
                displayWord = viewModelTrans.uiState.collectAsState().value,
                uiState = viewModelTrans.translatedContent.collectAsState().value,
                permissionState = permissionState,
                updateText = viewModelTrans::updateText,
                changeState = viewModelTrans::handleCameraEvent,
                handleUiState = viewModelTrans::updateUiState,
                attemptToSearchForNewWord = viewModelTrans::updateTranslation,
                updateLanguage = viewModelTrans::updateLanguage,
            )
        }
        composable("Settings") {
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
    takingPicture: @Composable () -> Unit,
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
        takingPicture()
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MeaningScreen(
    displayWord: MeaningState,
    uiState: MeaningContent,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    availableLanguages: Map<String, Int>,
    permissionState: MultiplePermissionsState,
    updateText: (String) -> Unit,
    changeState: (CameraEvent) -> Unit,
    attemptToSearchForNewWord: (String, String, Context) -> Unit,
    handleUiState: (UiState) -> Unit,
    changeLanguage: (String) -> Unit,
    clickEvent: (ClickEvent) -> Unit,
) {
    val context = LocalContext.current
    val databaseHelper = MyDatabaseHelper(context)
    val outputDirectory = LocalContext.current.filesDir
    val takePhotoProv = PictureTakerImpl()
    MeaningComposable(
        displayWord = displayWord,
        uiState = uiState,
        availableLanguages = availableLanguages,
        updateDisplayWord = updateText,
        handleUiState = handleUiState,
        attemptToSearchForNewWord = { first: String, second: String -> attemptToSearchForNewWord(first, second, context) },
        changeLanguage = changeLanguage,
        clickForward = { clickEvent(ClickEvent.Next(context)) },
        clickBackward = { clickEvent(ClickEvent.Previous(context)) },
        isFirst = isFirst,
        isLast = isLast,
    ) {
        CameraPermission(
            permissionState = permissionState,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                TakingPicture(
                    outputDirectory = outputDirectory,
                    changeState = changeState,
                    takePhoto = takePhotoProv::takePicture,
                    onBackClick = { handleUiState(UiState.SEARCH_STATE) },
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SearchScreenFull(
    displayWord: TranslationState,
    uiState: TranslationContents,
    permissionState: MultiplePermissionsState,
    updateText: (String) -> Unit,
    changeState: (CameraEvent) -> Unit,
    handleUiState: (UiState) -> Unit,
    attemptToSearchForNewWord: () -> Unit,
    updateLanguage: (String) -> Unit,
) {
    val outputDirectory = LocalContext.current.filesDir
    val takePhotoProv = PictureTakerImpl()
    fun trickeryThatNeedsToChange(a: String, b: String) {
        attemptToSearchForNewWord()
    }
    TranslationComposable(
        displayWord = displayWord,
        uiState = uiState,
        updateDisplayWord = updateText,
        handleUiState = handleUiState,
        attemptToSearchForNewWord = ::trickeryThatNeedsToChange,
        updateLanguage = updateLanguage,
    ) {
        CameraPermission(
            permissionState = permissionState,
        ) {
            TakingPicture(
                outputDirectory = outputDirectory,
                changeState = changeState,
                takePhoto = takePhotoProv::takePicture,
                onBackClick = { handleUiState(UiState.SEARCH_STATE) },
            )
        }
    }
}

@Composable
fun HomeScreen(
    currentlyDisplayed: HomeState,
    onClickNext: () -> Unit = {},
    onClickPrevious: () -> Unit = {},
    isEmpty: (Context) -> Boolean = { false },
) {
    ComposeBox(
        currentlyDisplayed.lastSearches,
        currentlyDisplayed.isfirst,
        currentlyDisplayed.islast,
        onClickNext,
        onClickPrevious,
        isEmpty,
    )
}
