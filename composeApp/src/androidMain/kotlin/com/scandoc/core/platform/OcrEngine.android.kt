package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

// Android actual — ML Kit TextRecognition (Phase 6)
actual class OcrEngine {
    actual suspend fun recognize(imageBytes: ByteArray): OcrResult =
        OcrResult(fullText = "", blocks = emptyList(), confidence = 0f, language = null)
}
