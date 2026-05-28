package com.scandoc.domain.usecase.camera

import com.scandoc.domain.platform.CameraController
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome

class StopCameraUseCase(private val cameraController: CameraController) {
    suspend operator fun invoke(): Outcome<Unit> =
        runCatching { cameraController.stop() }.toOutcome()
}
