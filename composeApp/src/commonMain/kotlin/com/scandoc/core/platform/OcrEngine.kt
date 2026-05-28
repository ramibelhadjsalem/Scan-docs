package com.scandoc.core.platform

import com.scandoc.domain.model.OcrResult

/**
 * Contract for OCR text recognition. Use cases and fakes depend on this.
 */
interface OcrEngine {
    suspend fun recognize(imageBytes: ByteArray): OcrResult
}

/**
 * Platform-specific OCR implementation.
 * Android: ML Kit TextRecognition.
 * iOS: Vision VNRecognizeTextRequest.
 */
expect class PlatformOcrEngine() : OcrEngine
