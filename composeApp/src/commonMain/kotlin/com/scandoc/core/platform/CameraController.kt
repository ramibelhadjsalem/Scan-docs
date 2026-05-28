package com.scandoc.core.platform

import kotlinx.coroutines.flow.Flow

/**
 * Platform-specific camera implementation.
 * Android: CameraX with ImageAnalysis use case.
 * iOS: AVCaptureSession with AVCaptureVideoDataOutput.
 */
expect class PlatformCameraController() : com.scandoc.domain.platform.CameraController {
    override val frames: Flow<ByteArray>
    override suspend fun start()
    override suspend fun stop()
    override suspend fun capture(): ByteArray
    override suspend fun setFlash(enabled: Boolean)
}
