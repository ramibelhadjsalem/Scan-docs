package com.scandoc.fake

import com.scandoc.core.platform.CameraController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeCameraController : CameraController {
    var captureResult: ByteArray = ByteArray(100)
    var shouldThrow = false
    var lastFlashState: Boolean? = null

    override val frames: Flow<ByteArray> = emptyFlow()

    override suspend fun start() = Unit

    override suspend fun stop() = Unit

    override suspend fun capture(): ByteArray {
        if (shouldThrow) throw IllegalStateException("Camera failure")
        return captureResult
    }

    override suspend fun setFlash(enabled: Boolean) {
        lastFlashState = enabled
    }
}
