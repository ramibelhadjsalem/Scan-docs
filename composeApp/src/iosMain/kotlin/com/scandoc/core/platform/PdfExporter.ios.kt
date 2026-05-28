package com.scandoc.core.platform

import com.scandoc.domain.model.Page

/**
 * iOS actual for [PdfExporter].
 * PDFKit bridging from Kotlin/Native is deferred to a future iteration. This stub returns
 * [outputPath] unchanged so that callers receive a valid (though empty) file path and can
 * proceed without crashing.
 */
actual class PdfExporter : PdfExportAccess {
    actual suspend fun export(pages: List<Page>, outputPath: String): String = outputPath
}
