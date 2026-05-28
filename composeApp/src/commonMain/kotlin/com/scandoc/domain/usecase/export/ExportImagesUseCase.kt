package com.scandoc.domain.usecase.export

import com.scandoc.domain.platform.AppFileSystem
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
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
