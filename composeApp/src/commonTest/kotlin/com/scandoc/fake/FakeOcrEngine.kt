package com.scandoc.fake

import com.scandoc.domain.model.OcrResult
import com.scandoc.domain.platform.OcrEngine

class FakeOcrEngine : OcrEngine {
    var shouldFail = false
    var resultToReturn = OcrResult(
        fullText = "Fake OCR text",
        blocks = emptyList(),
        confidence = 0.9f,
        language = "en",
    )

    override suspend fun recognize(imageBytes: ByteArray): OcrResult {
        if (shouldFail) throw RuntimeException("fake OCR failure")
        return resultToReturn
    }
}
