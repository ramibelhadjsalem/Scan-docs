package com.scandoc.domain.usecase.ocr

import com.scandoc.domain.platform.OcrEngine
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
import com.scandoc.domain.model.OcrResult

class RunOcrUseCase(
    private val ocrEngine: OcrEngine,
) {
    suspend operator fun invoke(imageBytes: ByteArray): Outcome<OcrResult> =
        runCatching { ocrEngine.recognize(imageBytes) }.toOutcome()
}
