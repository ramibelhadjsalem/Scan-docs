package com.scandoc.domain.usecase.camera

import com.scandoc.domain.platform.CameraController
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome

class CaptureFrameUseCase(
    private val cameraController: CameraController,
) {
    suspend operator fun invoke(): Outcome<ByteArray> =
        runCatching { cameraController.capture() }.toOutcome()
}
