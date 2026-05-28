package com.scandoc.domain.usecase.crop

import com.scandoc.core.platform.ImageProcessor
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
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
