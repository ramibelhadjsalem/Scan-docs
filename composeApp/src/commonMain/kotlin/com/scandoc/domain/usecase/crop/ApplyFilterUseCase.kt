package com.scandoc.domain.usecase.crop

import com.scandoc.domain.platform.ImageProcessor
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
import com.scandoc.domain.model.Filter

class ApplyFilterUseCase(
    private val imageProcessor: ImageProcessor,
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        filter: Filter,
    ): Outcome<ByteArray> =
        runCatching { imageProcessor.applyFilter(imageBytes, filter) }.toOutcome()
}
