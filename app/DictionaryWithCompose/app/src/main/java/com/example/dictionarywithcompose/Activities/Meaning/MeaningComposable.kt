package com.example.dictionarywithcompose.Activities.Meaning // ktlint-disable package-name

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dictionarywithcompose.Activities.Common.Speech.ButtonForSpeech
import com.example.dictionarywithcompose.Activities.HomeScreen.Arrows
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningContent
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.MeaningState
import com.example.dictionarywithcompose.Activities.Common.Speech.SpeechRecognizerContract
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.UiState

@Composable
fun MeaningComposable(
    displayWord: MeaningState,
    uiState: MeaningContent,
    availableLanguages: Map<String, Int>,
    updateDisplayWord: (word: String) -> Unit,
    handleUiState: (uiState: UiState) -> Unit,
    attemptToSearchForNewWord: (word: String, lang: String) -> Unit,
    changeLanguage: (String) -> Unit,
    clickForward: () -> Unit,
    clickBackward: () -> Unit,
    isFirst: Boolean,
    isLast: Boolean,
    CameraView: @Composable () -> Unit,
) {
    val value = LocalContext.current
    when (displayWord.uiState) {
        UiState.SEARCH_STATE -> {
            SearchState(
                modifier = Modifier.fillMaxWidth(),
                displayWord = displayWord,
                attemptToSearchForNewWord = { word, lang ->
                    attemptToSearchForNewWord(word, lang)
                },
                uiState = uiState,
                clickBackward,
                clickForward,
                isFirst,
                isLast,
            ) {
                InputText(
                    viewModelState = displayWord.displayText,
                    language = displayWord.lang,
                    updateDisplayWord = updateDisplayWord,
                    searchFuntion =
                    attemptToSearchForNewWord,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ButtonForSpeech(changeText = {
                            updateDisplayWord(it)
                        })
                        IconButton(onClick = {
                            handleUiState(UiState.TAKING_PHOTO)
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Camera,
                                contentDescription = "Camera",
                            )
                        }
                        IconDropDown(
                            availableLanguages = availableLanguages,
                            changeLanguage = changeLanguage,
                        )
                    }
                }
            }
        }
        UiState.TAKING_PHOTO -> {
            CameraView()
        }
    }
}

@Composable
fun InputText(viewModelState: String, language: String, updateDisplayWord: (word: String) -> Unit, searchFuntion: (String, String) -> Unit, trailingIcon: @Composable () -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth().padding(32.dp, 0.dp),
        value = viewModelState,
        onValueChange = {
            updateDisplayWord(it)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                Log.v("MeaningComposable", "Search button clicked")
                Log.v("MeaningComposable", "Word: $viewModelState")
                searchFuntion(viewModelState, language)
            },
        ),
        trailingIcon = trailingIcon,
    )
}

@Composable
fun SearchState(
    modifier: Modifier,
    displayWord: MeaningState,
    attemptToSearchForNewWord: (word: String, lang: String) -> Unit,
    uiState: MeaningContent,
    clickBackward: () -> Unit,
    clickForward: () -> Unit,
    isfirst: Boolean,
    islast: Boolean,
    searchBar: @Composable () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                searchBar()
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        attemptToSearchForNewWord(displayWord.displayText, displayWord.lang)
                    },
                ) {
                    Text(text = "Search")
                }
                SearchPageBody(modifier = Modifier.weight(1f)) {
                    DisplayWordMeaning(uiState)
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Arrows(isfirst = isfirst, islast = islast, onClickPrevious = clickBackward, onClickNext = clickForward)
        }
    }
}

@Composable
fun SearchPageBody(modifier: Modifier, DisplayWord: @Composable () -> Unit) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DisplayWord()
        }
    }
}

@Composable
fun DisplayWordMeaning(displayWord: MeaningContent) {
    Column(
        Modifier.fillMaxWidth().background(color = MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val value = LocalContext.current
        Text(
            text = displayWord.text,
            style = TextStyle(
                fontSize = 45.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Meaning:",
            style = TextStyle(
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Text(
            text = displayWord.meaning,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
            ),
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Example:",
            style = TextStyle(
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
        )
        Text(
            text = displayWord.example,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
            ),
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "I just found the meaning to the word ${displayWord.text}!\n" +
                        "It means: ${displayWord.meaning}\n" +
                        "It could be used in a sentence such as :\n" +
                        displayWord.example,
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(value, shareIntent, null)
        }) {
            Text(text = "Share")
        }
    }
}


@Composable
fun DropDownMenuMaker(
    modifier: Modifier,
    expanded: Boolean,
    changeExpanded: (Boolean) -> Unit,
    availableLanguages: Map<String, Int>,
    onCLick: (String) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { changeExpanded(false) },
        modifier = modifier
            .wrapContentSize(Alignment.TopEnd).padding(8.dp),
    ) {
        val keys = availableLanguages.keys.toList()
        keys.forEach { key ->
            DropdownMenuItem(
                onClick = {
                    onCLick(key)
                },
                modifier = Modifier.size(64.dp, 40.dp).align(Alignment.Start),
            ) {
                Image(painter = painterResource(id = availableLanguages[key]!!), contentDescription = null, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun IconDropDown(
    availableLanguages: Map<String, Int>,
    changeLanguage: (String) -> Unit,
) {
    val currentLanguage = remember {
        mutableStateOf(availableLanguages.keys.minOf { it })
    }
    val first = remember { mutableStateOf(true) }
    val expanded = remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded.value = true
    }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (first.value) {
                Icon(
                    imageVector =
                    Icons.Default.MoreVert,
                    contentDescription = "More",
                )
            } else {
                Image(
                    painter = painterResource(id = availableLanguages[currentLanguage.value]!!),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
            DropDownMenuMaker(
                modifier = Modifier,
                expanded = expanded.value,
                changeExpanded = { expanded.value = it },
                availableLanguages = availableLanguages,
                onCLick = {
                    currentLanguage.value = it
                    expanded.value = false
                    first.value = false
                    changeLanguage(it)
                },
            )
        }
    }
}
