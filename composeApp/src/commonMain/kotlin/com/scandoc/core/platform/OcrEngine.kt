package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

/**
 * Platform-specific OCR implementation.
 * Android: ML Kit TextRecognition.
 * iOS: Vision VNRecognizeTextRequest.
 */
expect class PlatformOcrEngine() : com.scandoc.domain.platform.OcrEngine {
    override suspend fun recognize(imageBytes: ByteArray): OcrResult
}
