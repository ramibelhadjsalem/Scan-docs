package com.scandoc.domain.usecase.export

import com.scandoc.domain.platform.AppFileSystem
import com.scandoc.domain.platform.PdfExporter
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
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
