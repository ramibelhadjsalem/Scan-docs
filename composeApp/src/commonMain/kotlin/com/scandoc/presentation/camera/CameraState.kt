package com.scandoc.presentation.camera

import com.scandoc.domain.model.DocumentCorners

data class CameraState(
    val isActive: Boolean = false,
    val detectedCorners: DocumentCorners? = null,
    val isFlashOn: Boolean = false,
    val isAutoCapture: Boolean = true,
    val captureCountdown: Int? = null,
    val error: String? = null,
)

sealed interface CameraIntent {
    data object StartCamera : CameraIntent
    data object StopCamera : CameraIntent
    data object Capture : CameraIntent
    data object ToggleFlash : CameraIntent
    data class ToggleAutoCapture(val enabled: Boolean) : CameraIntent
}

sealed interface CameraEffect {
    data class NavigateToCrop(val imageBytes: ByteArray) : CameraEffect
    data class ShowError(val message: String) : CameraEffect
}
