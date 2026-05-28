package com.scandoc.domain.usecase.export

import com.scandoc.core.platform.AppFileSystem
import com.scandoc.core.platform.PdfExporter
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.domain.model.Document

class ExportPdfUseCase(
    private val pdfExporter: PdfExporter,
    private val fileSystem: AppFileSystem,
) {
    suspend operator fun invoke(document: Document): Outcome<String> =
        runCatching {
            val outputPath = "${fileSystem.documentsDir}/${document.name}.pdf"
            pdfExporter.export(document.pages, outputPath)
        }.toOutcome()
}
