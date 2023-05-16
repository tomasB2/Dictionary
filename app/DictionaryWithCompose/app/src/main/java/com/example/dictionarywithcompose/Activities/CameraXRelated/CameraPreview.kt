package com.example.dictionarywithcompose.Activities.CameraXRelated // ktlint-disable package-name

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Camera
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.CameraEvent
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.CameraState
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.WordState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.Locale
import java.util.concurrent.Executor

@Composable
fun CameraView(
    outputDirectory: File,
    executor: Executor,
    viewModelState: WordState,
    changeState: (CameraEvent) -> Unit,
    uptadeText: (String) -> Unit,

) {
    if (viewModelState.cameraState == CameraState.TEXT_BOX) {
        InputText(viewModelState, updateText = uptadeText) {
            TrailingIcon {
                changeState(CameraEvent.ChangeToPictureScreen)
            }
        }
    } else {
        TakingPicture(
            outputDirectory = outputDirectory,
            executor = executor,
            changeState = changeState,
        )
    }
}
private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    changeState: (CameraEvent) -> Unit,
) {
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.UK).format(System.currentTimeMillis()) + ".jpg",
    )
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.v("CameraX", exception.toString())
                changeState(CameraEvent.Error("Failed to take picture"))
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val bitmap: Bitmap = BitmapFactory.decodeFile(savedUri.path)
                recognizer.process(InputImage.fromBitmap(bitmap, 0))
                    .addOnSuccessListener { visionText ->
                        Log.v("CameraX", visionText.text)
                        changeState(CameraEvent.PictureTaken(visionText.text))
                    }
                    .addOnFailureListener {
                        Log.v("CameraX", it.toString())
                        changeState(CameraEvent.Error("Failed to recognize text"))
                    }
            }
        },
    )
}

@Composable
fun TakingPicture(
    outputDirectory: File,
    executor: Executor,
    changeState: (CameraEvent) -> Unit,
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture,
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize().padding(40.dp)) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirectory,
                    executor = executor,
                    changeState = changeState,
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = "Take picture",
                    tint = Color.White,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape),
                )
            },
        )
    }
}

@Composable
fun InputText(viewModelState: WordState, updateText: (String) -> Unit, trailingIcon: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row() {
            TextField(
                value = viewModelState.text,
                onValueChange = {
                    updateText(it)
                },
                trailingIcon = trailingIcon,
            )
        }
    }
}

@Composable
fun TrailingIcon(
    changeState: () -> Unit,
) {
    IconButton(
        onClick = changeState,
        content = { Icon(imageVector = Icons.Sharp.Camera, contentDescription = "Change to Camera") },
    )
}
