package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

/**
 * Platform-specific image processing implementation.
 * Android: ML Kit / built-in bitmap ops.
 * iOS: Vision VNDetectRectanglesRequest + CoreImage.
 */
expect class PlatformImageProcessor() : com.scandoc.domain.platform.ImageProcessor {
    override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners?
    override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray
    override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray
}
