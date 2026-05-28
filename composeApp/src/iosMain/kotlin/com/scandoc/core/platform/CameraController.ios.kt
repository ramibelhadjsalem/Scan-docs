package com.scandoc.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// iOS actual — AVFoundation (Phase 6)
actual class CameraController {
    actual val frames: Flow<ByteArray> = emptyFlow()
    actual suspend fun start() {}
    actual suspend fun stop() {}
    actual suspend fun capture(): ByteArray = ByteArray(0)
    actual suspend fun setFlash(enabled: Boolean) {}
}
