package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

/**
 * iOS actual for [OcrEngine].
 * Vision VNRecognizeTextRequest requires ObjC bridging deferred to a future iteration.
 * Returns an empty [OcrResult] with zero confidence so callers can handle the no-op case
 * gracefully without crashing.
 */
actual class PlatformOcrEngine actual constructor() : OcrEngine {
    override suspend fun recognize(imageBytes: ByteArray): OcrResult =
        OcrResult(
            fullText = "",
            blocks = emptyList(),
            confidence = 0f,
            language = null,
        )
}
