package com.scandoc.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// iOS actual — AVFoundation camera (Phase 7)
actual class PlatformCameraController() : com.scandoc.domain.platform.CameraController {
    actual override val frames: Flow<ByteArray> = emptyFlow()
    actual override suspend fun start() {}
    actual override suspend fun stop() {}
    actual override suspend fun capture(): ByteArray = ByteArray(0)
    actual override suspend fun setFlash(enabled: Boolean) {}
}
