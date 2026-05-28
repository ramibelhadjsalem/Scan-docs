package com.scandoc.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.camera.CaptureFrameUseCase
import com.scandoc.domain.usecase.camera.DetectEdgesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel(
    private val captureFrame: CaptureFrameUseCase,
    private val detectEdges: DetectEdgesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state: StateFlow<CameraState> = _state.asStateFlow()

    private val _effects = Channel<CameraEffect>(Channel.BUFFERED)
    val effects: Flow<CameraEffect> = _effects.receiveAsFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            CameraIntent.StartCamera -> _state.update { it.copy(isActive = true) }
            CameraIntent.StopCamera -> _state.update { it.copy(isActive = false) }
            CameraIntent.Capture -> capture()
            CameraIntent.ToggleFlash -> _state.update { it.copy(isFlashOn = !it.isFlashOn) }
            is CameraIntent.ToggleAutoCapture -> _state.update { it.copy(isAutoCapture = intent.enabled) }
        }
    }

    private fun capture() {
        viewModelScope.launch {
            when (val result = captureFrame()) {
                is Outcome.Success -> _effects.send(CameraEffect.NavigateToCrop(result.value))
                is Outcome.Failure -> _effects.send(
                    CameraEffect.ShowError(result.error.message ?: "Capture failed"),
                )
            }
        }
    }
}
