package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import kotlinx.coroutines.flow.Flow

/**
 * Contract for camera access. Use cases and fakes depend on this.
 */
interface CameraController {
    val frames: Flow<ByteArray>
    suspend fun start()
    suspend fun stop()
    suspend fun capture(): ByteArray
    suspend fun setFlash(enabled: Boolean)
}

/**
 * Platform-specific camera implementation.
 * Android: CameraX with ImageAnalysis use case.
 * iOS: AVCaptureSession with AVCaptureVideoDataOutput.
 */
expect class PlatformCameraController() : CameraController
