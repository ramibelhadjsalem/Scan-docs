package com.scandoc.domain.usecase.crop

import com.scandoc.core.platform.ImageProcessor
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.domain.model.DocumentCorners

class ApplyPerspectiveUseCase(
    private val imageProcessor: ImageProcessor,
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        corners: DocumentCorners,
    ): Outcome<ByteArray> =
        runCatching { imageProcessor.applyPerspective(imageBytes, corners) }.toOutcome()
}
