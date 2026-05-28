package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

/**
 * Platform-specific image processing.
 * Android: ML Kit Document Scanner / built-in bitmap ops.
 * iOS: Vision VNDetectRectanglesRequest + CoreImage CIPerspectiveCorrection.
 */
expect class ImageProcessor {
    suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners?
    suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray
    suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray
}
