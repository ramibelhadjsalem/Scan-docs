package com.scandoc.core.platform

/**
 * Platform-specific PDF exporter.
 * Android: android.graphics.pdf.PdfDocument.
 * iOS: PDFKit PDFDocument.
 */
expect class PdfExporter : com.scandoc.domain.platform.PdfExporter {
    override suspend fun export(pages: List<com.scandoc.domain.model.Page>, outputPath: String): String
}
