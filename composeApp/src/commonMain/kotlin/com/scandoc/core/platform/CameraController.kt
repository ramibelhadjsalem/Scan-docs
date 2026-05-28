package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import kotlinx.coroutines.flow.Flow

/**
 * Platform-specific camera controller.
 * Android: CameraX with ImageAnalysis use case.
 * iOS: AVCaptureSession with AVCaptureVideoDataOutput.
 */
expect class CameraController {
    val frames: Flow<ByteArray>
    suspend fun start()
    suspend fun stop()
    suspend fun capture(): ByteArray
    suspend fun setFlash(enabled: Boolean)
}
