package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

/**
 * Platform-specific OCR engine.
 * Android: ML Kit TextRecognition.
 * iOS: Vision VNRecognizeTextRequest.
 */
expect class OcrEngine {
    suspend fun recognize(imageBytes: ByteArray): OcrResult
}
