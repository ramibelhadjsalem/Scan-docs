package com.scandoc.core.platform

import com.scandoc.domain.model.Page
import okio.FileSystem
import okio.Path.Companion.toPath

actual class PdfExporter : com.scandoc.domain.platform.PdfExporter {
    actual override suspend fun export(pages: List<Page>, outputPath: String): String {
        val path = outputPath.toPath()
        path.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
        FileSystem.SYSTEM.write(path) {
            write(pdfBytesFor(pages))
        }
        return outputPath
    }

    private fun pdfBytesFor(pages: List<Page>): ByteArray {
        val text = pages.joinToString(separator = "\n") { page ->
            page.ocrResult?.fullText?.takeIf { it.isNotBlank() } ?: "ScanDoc page ${page.orderIndex + 1}"
        }.ifBlank { "ScanDoc export" }
        return """
            %PDF-1.4
            1 0 obj
            << /Type /Catalog /Pages 2 0 R >>
            endobj
            2 0 obj
            << /Type /Pages /Kids [3 0 R] /Count 1 >>
            endobj
            3 0 obj
            << /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>
            endobj
            4 0 obj
            << /Length 72 >>
            stream
            BT /F1 18 Tf 72 760 Td (${text.sanitizePdfText()}) Tj ET
            endstream
            endobj
            5 0 obj
            << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>
            endobj
            trailer
            << /Root 1 0 R >>
            %%EOF
        """.trimIndent().encodeToByteArray()
    }

    private fun String.sanitizePdfText(): String =
        replace("\\", "\\\\")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("\n", " ")
            .take(220)
}
