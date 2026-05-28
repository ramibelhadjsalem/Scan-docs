package com.scandoc.core.platform

import com.scandoc.domain.model.Page

// Android actual — android.graphics.pdf.PdfDocument (Phase 6)
actual class PdfExporter : com.scandoc.domain.platform.PdfExporter {
    actual override suspend fun export(pages: List<Page>, outputPath: String): String = outputPath
}
