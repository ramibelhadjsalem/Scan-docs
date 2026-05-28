package com.scandoc.core.platform

import com.scandoc.domain.model.Page
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSURL
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIImage

/**
 * iOS actual — uses PDFKit to produce a real PDF from scanned page images.
 * Each page image is embedded as a full-page PDF page.
 * Requires iOS 11+.
 */
actual class PdfExporter : com.scandoc.domain.platform.PdfExporter {

    actual override suspend fun export(pages: List<Page>, outputPath: String): String {
        val outputDir = outputPath.toPath().parent
        outputDir?.let { FileSystem.SYSTEM.createDirectories(it) }

        val document = PDFDocument()

        val pagesToExport = pages.ifEmpty {
            return writeFallbackPdf(outputPath)
        }

        pagesToExport.forEachIndexed { index, page ->
            val uiImage = UIImage.imageWithContentsOfFile(page.imagePath) ?: return@forEachIndexed
            val pdfPage = PDFPage(image = uiImage) ?: return@forEachIndexed
            document.insertPage(pdfPage, atIndex = index.toULong())
        }

        val url = NSURL.fileURLWithPath(outputPath)
        document.writeToURL(url)
        return outputPath
    }

    private fun writeFallbackPdf(outputPath: String): String {
        val path = outputPath.toPath()
        path.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
        FileSystem.SYSTEM.write(path) {
            write(minimalPdfBytes())
        }
        return outputPath
    }

    private fun minimalPdfBytes(): ByteArray =
        """
        %PDF-1.4
        1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj
        2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj
        3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] >> endobj
        trailer << /Root 1 0 R >>
        %%EOF
        """.trimIndent().encodeToByteArray()
}
