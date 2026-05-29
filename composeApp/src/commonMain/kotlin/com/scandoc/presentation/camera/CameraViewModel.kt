package com.scandoc.presentation.camera

import androidx.lifecycle.viewModelScope
import com.scandoc.domain.platform.CameraController
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.camera.CaptureFrameUseCase
import com.scandoc.domain.usecase.camera.DetectEdgesUseCase
import com.scandoc.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class CameraViewModel(
    private val cameraController: CameraController,
    private val captureFrame: CaptureFrameUseCase,
    private val detectEdges: DetectEdgesUseCase,
) : BaseViewModel<CameraState, CameraEffect>(CameraState()) {

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            CameraIntent.StartCamera -> {
                updateState { it.copy(isActive = true) }
                viewModelScope.launch {
                    runCatching { cameraController.start() }
                }
            }
            CameraIntent.StopCamera -> {
                updateState { it.copy(isActive = false) }
                viewModelScope.launch {
                    runCatching { cameraController.stop() }
                }
            }
            CameraIntent.Capture -> capture()
            CameraIntent.ToggleFlash -> {
                val newFlash = !currentState.isFlashOn
                updateState { it.copy(isFlashOn = newFlash) }
                viewModelScope.launch {
                    runCatching { cameraController.setFlash(newFlash) }
                }
            }
            is CameraIntent.ToggleAutoCapture -> updateState { it.copy(isAutoCapture = intent.enabled) }
            is CameraIntent.SelectMode -> updateState { it.copy(selectedMode = intent.mode) }
            is CameraIntent.SelectFeature -> updateState { it.copy(selectedFeature = intent.feature) }
            CameraIntent.ToggleHd -> updateState { it.copy(isHdMode = !it.isHdMode) }
        }
    }

    private fun capture() {
        viewModelScope.launch {
            when (val result = captureFrame()) {
                is Outcome.Success -> sendEffect(CameraEffect.NavigateToCrop(result.value))
                is Outcome.Failure -> sendEffect(
                    CameraEffect.ShowError(result.error.message ?: "Capture failed"),
                )
            }
        }
    }
}
