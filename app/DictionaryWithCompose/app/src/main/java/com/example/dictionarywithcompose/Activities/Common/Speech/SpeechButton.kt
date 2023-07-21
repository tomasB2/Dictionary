package com.example.dictionarywithcompose.Activities.Common.Speech

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ButtonForSpeech(changeText: (String) -> Unit) {
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {
            it?.get(0)?.let { it1 -> Log.v("Speech", it1) }
            if (it != null) changeText(it[0])
        },
    )

    Icon(
        imageVector = Icons.Filled.Mic,
        contentDescription = "Speech",
        modifier = Modifier
            .padding(16.dp).clickable {
                speechRecognizerLauncher.launch(Unit)
            },
    )
}