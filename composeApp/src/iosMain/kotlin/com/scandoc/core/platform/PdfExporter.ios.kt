package com.scandoc.core.platform

import com.scandoc.domain.model.Page

// iOS actual — PDFKit (Phase 6)
actual class PdfExporter : com.scandoc.domain.platform.PdfExporter {
    actual override suspend fun export(pages: List<Page>, outputPath: String): String = outputPath
}
