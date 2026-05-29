package com.scandoc.presentation.camera

import com.scandoc.domain.model.DocumentCorners

data class CameraState(
    val isActive: Boolean = false,
    val detectedCorners: DocumentCorners? = null,
    val isFlashOn: Boolean = false,
    val isAutoCapture: Boolean = true,
    val selectedMode: CaptureMode = CaptureMode.Single,
    val selectedFeature: CameraFeature = CameraFeature.Document,
    val isHdMode: Boolean = true,
    val captureCountdown: Int? = null,
    val error: String? = null,
)

sealed interface CaptureMode {
    val label: String

    data object Single : CaptureMode {
        override val label: String = "Simple"
    }

    data object Lot : CaptureMode {
        override val label: String = "Lot"
    }
}

sealed interface CameraFeature {
    val label: String

    data object Document : CameraFeature {
        override val label: String = "Document"
    }

    data object IdCard : CameraFeature {
        override val label: String = "ID Card"
    }

    data object Receipt : CameraFeature {
        override val label: String = "Receipt"
    }

    data object QrCode : CameraFeature {
        override val label: String = "QR Code"
    }

    data object Whiteboard : CameraFeature {
        override val label: String = "Whiteboard"
    }

    companion object {
        val all: List<CameraFeature> = listOf(Document, IdCard, Receipt, QrCode, Whiteboard)
    }
}

sealed interface CameraIntent {
    data object StartCamera : CameraIntent
    data object StopCamera : CameraIntent
    data object Capture : CameraIntent
    data object ToggleFlash : CameraIntent
    data class ToggleAutoCapture(val enabled: Boolean) : CameraIntent
    data class SelectMode(val mode: CaptureMode) : CameraIntent
    data class SelectFeature(val feature: CameraFeature) : CameraIntent
    data object ToggleHd : CameraIntent
}

sealed interface CameraEffect {
    data class NavigateToCrop(val imageBytes: ByteArray) : CameraEffect
    data class ShowError(val message: String) : CameraEffect
}
