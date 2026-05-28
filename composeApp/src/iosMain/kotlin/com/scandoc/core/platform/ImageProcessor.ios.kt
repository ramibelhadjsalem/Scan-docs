package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

// iOS actual — CoreImage / Vision (Phase 6)
actual class PlatformImageProcessor actual constructor() : ImageProcessor {
    override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? = null
    override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray = imageBytes
    override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray = imageBytes
}
