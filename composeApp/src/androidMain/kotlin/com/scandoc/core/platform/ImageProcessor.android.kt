package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

// Android actual — ML Kit / bitmap ops (Phase 6)
actual class ImageProcessor {
    actual suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? = null
    actual suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray = imageBytes
    actual suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray = imageBytes
}
