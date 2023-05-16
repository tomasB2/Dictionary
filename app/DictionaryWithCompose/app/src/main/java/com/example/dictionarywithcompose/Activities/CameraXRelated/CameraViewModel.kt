package com.example.dictionarywithcompose.Activities.CameraXRelated // ktlint-disable package-name

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.CameraEvent
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.CameraState
import com.example.dictionarywithcompose.Activities.CameraXRelated.datatypes.WordState
import kotlinx.coroutines.flow.MutableStateFlow

class CameraViewModel() : ViewModel() {
    val state = MutableStateFlow(WordState(text = "", cameraState = CameraState.TEXT_BOX))
    private val counter = MutableStateFlow(0) // for debug purposes. Was having problems with view Model LifeCycle management
    fun handleEvent(cameraEvent: CameraEvent) {
        Log.v("CameraViewModel", "Counter: ${counter.value}")
        counter.value += 1
        when (cameraEvent) {
            is CameraEvent.PictureTaken -> {
                Log.v("CameraViewModel", "Picture taken: ${cameraEvent.text}")
                state.value = state.value.copy(text = cameraEvent.text, cameraState = CameraState.TEXT_BOX)
                return
            }
            is CameraEvent.Error -> {
                Log.v("CameraViewModel", "Error: ${cameraEvent.text}")
                state.value = state.value.copy(text = "Could not retreive text", cameraState = CameraState.TEXT_BOX)
                return
            }
            else -> {
                Log.v("CameraViewModel", "Change to picture screen")
                state.value = state.value.copy(text = "", cameraState = CameraState.TAKING_PICTURE)
            }
        }
    }
    fun updateText(text: String) {
        state.value = state.value.copy(text = text)
    }
}
