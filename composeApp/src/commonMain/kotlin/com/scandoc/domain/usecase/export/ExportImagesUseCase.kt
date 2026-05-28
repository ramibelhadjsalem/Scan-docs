package com.scandoc.domain.usecase.export

import com.scandoc.core.platform.AppFileSystem
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.ExportFormat

class ExportImagesUseCase(
    private val fileSystem: AppFileSystem,
) {
    suspend operator fun invoke(
        document: Document,
        format: ExportFormat,
    ): Outcome<List<String>> =
        runCatching {
            val ext = if (format == ExportFormat.PngImages) "png" else "jpg"
            document.pages.map { page ->
                "${fileSystem.documentsDir}/${document.id}_page${page.orderIndex}.$ext"
            }
        }.toOutcome()
}
