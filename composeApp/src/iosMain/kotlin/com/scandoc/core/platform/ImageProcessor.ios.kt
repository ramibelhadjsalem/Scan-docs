package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

// iOS actual — CoreImage / Vision (Phase 6)
actual class PlatformImageProcessor actual constructor() : com.scandoc.domain.platform.ImageProcessor {
    actual override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? = null
    actual override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray = imageBytes
    actual override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray = imageBytes
}
