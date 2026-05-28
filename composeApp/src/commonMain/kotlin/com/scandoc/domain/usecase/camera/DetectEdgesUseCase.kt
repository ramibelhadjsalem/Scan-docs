package com.scandoc.domain.usecase.camera

import com.scandoc.domain.platform.ImageProcessor
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
import com.scandoc.domain.model.DocumentCorners

class DetectEdgesUseCase(
    private val imageProcessor: ImageProcessor,
) {
    suspend operator fun invoke(imageBytes: ByteArray): Outcome<DocumentCorners?> =
        runCatching { imageProcessor.detectCorners(imageBytes) }.toOutcome()
}
