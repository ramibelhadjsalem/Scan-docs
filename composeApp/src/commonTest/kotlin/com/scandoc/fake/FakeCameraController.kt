package com.scandoc.fake

import com.scandoc.domain.platform.CameraController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeCameraController : CameraController {
    override val frames: Flow<ByteArray> = emptyFlow()
    var isStarted = false
    var isFlashOn = false
    var captureResult: ByteArray = ByteArray(10)

    override suspend fun start() { isStarted = true }
    override suspend fun stop() { isStarted = false }
    override suspend fun capture(): ByteArray = captureResult
    override suspend fun setFlash(enabled: Boolean) { isFlashOn = enabled }
}
