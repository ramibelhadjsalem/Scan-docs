package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

/**
 * Contract for image processing. Use cases and fakes depend on this.
 */
interface ImageProcessor {
    suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners?
    suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray
    suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray
}

/**
 * Platform-specific image processing implementation.
 * Android: ML Kit / built-in bitmap ops.
 * iOS: Vision VNDetectRectanglesRequest + CoreImage.
 */
expect class PlatformImageProcessor() : ImageProcessor
