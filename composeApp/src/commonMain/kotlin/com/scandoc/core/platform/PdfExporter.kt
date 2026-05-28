package com.scandoc.core.platform

import com.scandoc.domain.model.Page

interface PdfExportAccess {
    suspend fun export(pages: List<Page>, outputPath: String): String
}

/**
 * Platform-specific PDF exporter.
 * Android: android.graphics.pdf.PdfDocument.
 * iOS: PDFKit PDFDocument.
 */
expect class PdfExporter : PdfExportAccess
