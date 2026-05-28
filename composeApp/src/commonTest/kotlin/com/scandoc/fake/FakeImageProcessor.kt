package com.scandoc.fake

import com.scandoc.core.platform.ImageProcessor
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

class FakeImageProcessor : ImageProcessor {
    var cornersToReturn: DocumentCorners? = null
    var processedBytes: ByteArray = ByteArray(200)
    var shouldThrow = false
    var lastDetectBytes: ByteArray? = null
    var lastPerspectiveBytes: ByteArray? = null
    var lastFilterApplied: Filter? = null

    override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? {
        lastDetectBytes = imageBytes
        if (shouldThrow) throw IllegalStateException("Image processing failure")
        return cornersToReturn
    }

    override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray {
        lastPerspectiveBytes = imageBytes
        if (shouldThrow) throw IllegalStateException("Perspective failure")
        return processedBytes
    }

    override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray {
        lastFilterApplied = filter
        if (shouldThrow) throw IllegalStateException("Filter failure")
        return processedBytes
    }
}
