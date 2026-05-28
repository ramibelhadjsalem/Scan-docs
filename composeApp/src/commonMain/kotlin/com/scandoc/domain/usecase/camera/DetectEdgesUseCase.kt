package com.scandoc.domain.usecase.camera

import com.scandoc.core.platform.ImageProcessor
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.domain.model.DocumentCorners

class DetectEdgesUseCase(
    private val imageProcessor: ImageProcessor,
) {
    suspend operator fun invoke(imageBytes: ByteArray): Outcome<DocumentCorners?> =
        runCatching { imageProcessor.detectCorners(imageBytes) }.toOutcome()
}
