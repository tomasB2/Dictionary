package com.example.dictionarywithcompose.TakingPicture // ktlint-disable package-name

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.example.dictionarywithcompose.Activities.Meaning.DataTypes.CameraEvent
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.Locale
import java.util.concurrent.Executor

class PictureTakerImpl : PictureTaker {
    override fun takePicture(
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
                    processImage(savedUri, changeState)
                }
            },
        )
    }
    fun processImage(uri: Uri, changeState: (CameraEvent) -> Unit) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val bitmap: Bitmap = BitmapFactory.decodeFile(uri.path)

        recognizer.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { visionText ->
                changeState(CameraEvent.PictureTaken(visionText.text))
            }
            .addOnFailureListener {
                changeState(CameraEvent.Error("Failed to recognize text"))
            }
            .addOnCanceledListener {
                recognizer.close()
            }
    }
}
