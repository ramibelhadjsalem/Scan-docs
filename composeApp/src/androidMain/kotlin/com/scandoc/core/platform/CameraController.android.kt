package com.scandoc.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// Android actual — CameraX (Phase 6)
actual class PlatformCameraController actual constructor() : CameraController {
    override val frames: Flow<ByteArray> = emptyFlow()
    override suspend fun start() {}
    override suspend fun stop() {}
    override suspend fun capture(): ByteArray = ByteArray(0)
    override suspend fun setFlash(enabled: Boolean) {}
}
