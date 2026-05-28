package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

/**
 * iOS actual for [ImageProcessor].
 * CoreImage / Vision framework bridging from Kotlin/Native requires substantial ObjC interop
 * work deferred to a future iteration. [detectCorners] returns null (no detection), and both
 * [applyPerspective] and [applyFilter] are identity operations that return the input unchanged.
 */
actual class PlatformImageProcessor actual constructor() : ImageProcessor {
    override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? = null
    override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray = imageBytes
    override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray = imageBytes
}
