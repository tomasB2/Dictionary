package com.example.dictionarywithcompose.Activities.Meaning.DataTypes // ktlint-disable package-name

sealed class CameraEvent {
    class PictureTaken(val text: String) : CameraEvent()
    class Error(val text: String) : CameraEvent()
    object ChangeToPictureScreen : CameraEvent()
}
