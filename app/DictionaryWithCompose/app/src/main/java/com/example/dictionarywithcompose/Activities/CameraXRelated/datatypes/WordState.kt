package com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes // ktlint-disable package-name

data class WordState(
    val text: String = "",
    val cameraState: CameraState = CameraState.TEXT_BOX
)
