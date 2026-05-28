package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

// iOS actual — Vision VNRecognizeTextRequest (Phase 6)
actual class PlatformOcrEngine actual constructor() : OcrEngine {
    override suspend fun recognize(imageBytes: ByteArray): OcrResult =
        OcrResult(fullText = "", blocks = emptyList(), confidence = 0f, language = null)
}
