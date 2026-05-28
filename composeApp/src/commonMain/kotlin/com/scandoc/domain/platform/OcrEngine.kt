package com.scandoc.domain.platform

import com.scandoc.domain.model.OcrResult

interface OcrEngine {
    suspend fun recognize(imageBytes: ByteArray): OcrResult
}
