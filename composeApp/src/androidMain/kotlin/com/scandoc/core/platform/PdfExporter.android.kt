package com.scandoc.core.platform

import com.scandoc.domain.model.Page

// Android actual — android.graphics.pdf.PdfDocument (Phase 6)
actual class PdfExporter {
    actual suspend fun export(pages: List<Page>, outputPath: String): String = outputPath
}
