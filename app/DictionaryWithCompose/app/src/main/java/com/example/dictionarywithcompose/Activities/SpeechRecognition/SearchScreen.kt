package com.example.dictionarywithcompose.Activities.SpeechRecognition // ktlint-disable package-name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.MainScreenState
import com.example.dictionarywithcompose.Activities.SpeechRecognition.dataTypes.WordOnScreen

// ktlint-disable package-name

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel = viewModel<SearchViewModel>()
    val MainState = viewModel.uiState.collectAsState().value
    val handleEvent: (String) -> Unit = {
        viewModel.changeTextValue(it)
    }
    val displayWord = viewModel.DisplayedWord.collectAsState().value
    Column() {
        SearchBarVoiceSearch(modifier = Modifier, MainState = MainState, handleEvent = handleEvent)
        SearchPageBody(modifier = Modifier) {
            DisplayWordMeaning(displayWord)
        }
    }
}

@Composable
fun SearchPageBody(modifier: Modifier.Companion, DisplayWord: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {
            DisplayWord()
        }
    }
}

@Composable
fun SearchBarVoiceSearch(modifier: Modifier, MainState: MainScreenState, handleEvent: (String) -> Unit) {
    Row(
        modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
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
fun DisplayWordMeaning(displayWord: WordOnScreen) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = displayWord.word,
            style = TextStyle(
                fontSize = 60.sp,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Meaning:",
            style = TextStyle(
                fontSize = 30.sp,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Text(
            text = displayWord.meaning,
            style = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            ),
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Example:",
            style = TextStyle(
                fontSize = 30.sp,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Text(
            text = displayWord.example,
            style = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
            ),
        )
    }
}
