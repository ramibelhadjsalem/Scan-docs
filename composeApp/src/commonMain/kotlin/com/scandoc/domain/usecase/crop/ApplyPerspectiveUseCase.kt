package com.scandoc.domain.usecase.crop

import com.scandoc.domain.platform.ImageProcessor
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
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
