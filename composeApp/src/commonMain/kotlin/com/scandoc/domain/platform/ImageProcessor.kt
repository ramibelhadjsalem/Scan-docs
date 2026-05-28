package com.scandoc.domain.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

interface ImageProcessor {
    suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners?
    suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray
    suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray
}
