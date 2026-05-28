package com.scandoc.domain.usecase.export

import com.scandoc.core.platform.FileSystemAccess
import com.scandoc.core.platform.PdfExportAccess
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.domain.model.Document

class ExportPdfUseCase(
    private val pdfExporter: PdfExportAccess,
    private val fileSystem: FileSystemAccess,
) {
    suspend operator fun invoke(document: Document): Outcome<String> =
        runCatching {
            val outputPath = "${fileSystem.documentsDir}/${document.name}.pdf"
            pdfExporter.export(document.pages, outputPath)
        }.toOutcome()
}
