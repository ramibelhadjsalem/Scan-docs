package com.scandoc.domain.usecase.camera

import com.scandoc.core.platform.CameraController
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome

class CaptureFrameUseCase(
    private val cameraController: CameraController,
) {
    suspend operator fun invoke(): Outcome<ByteArray> =
        runCatching { cameraController.capture() }.toOutcome()
}
