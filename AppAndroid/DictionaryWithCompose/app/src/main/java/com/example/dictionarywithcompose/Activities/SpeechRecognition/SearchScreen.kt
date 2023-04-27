package com.example.dictionarywithcompose.Activities.SpeechRecognition // ktlint-disable package-name

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.MainScreenState

// ktlint-disable package-name

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel = viewModel<MainViewModel>()
    val MainState = viewModel.uiState.collectAsState().value
    val handleEvent: (String) -> Unit = {
        viewModel.changeTextValue(it)
    }
    SearchBarVoiceSearch(modifier = Modifier, MainState = MainState, handleEvent = handleEvent)
    //SearchPageBody(modifier = Modifier, MainState = MainState, navController = navController)
}

@Composable
fun SearchPageBody(modifier: Modifier.Companion, MainState: MainScreenState, navController: NavHostController) {
    Card(modifier = modifier.padding(16.dp).fillMaxSize()) {
    }
}

@Composable
fun SearchBarVoiceSearch(modifier: Modifier, MainState: MainScreenState, handleEvent: (String) -> Unit) {
    Row(modifier.padding(16.dp).fillMaxSize()) {
        SearchBar(modifier = modifier, MainState = MainState, handleEvent = handleEvent) {
            ButtonForSpeech(handleEvent)
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier, MainState: MainScreenState, handleEvent: (String) -> Unit, trailingIcon: @Composable () -> Unit) {
    TextField(
        value = MainState.text,
        onValueChange = {
            handleEvent(it)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,

        ),
        /*onImeActionPerformed ={
            // TODO("add search funtionality")
        },*/
        trailingIcon = trailingIcon,
    )
}

@Composable
fun DisplayWordMeaning() {

}
