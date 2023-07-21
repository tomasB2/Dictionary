package com.example.dictionarywithcompose.TakingPicture

import androidx.camera.core.ImageCapture
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.CameraEvent
import java.io.File
import java.util.concurrent.Executor

interface PictureTaker {
    fun takePicture(
        filenameFormat: String,
        imageCapture: ImageCapture,
        outputDirectory: File,
        executor: Executor,
        changeState: (CameraEvent) -> Unit,
    )
}
