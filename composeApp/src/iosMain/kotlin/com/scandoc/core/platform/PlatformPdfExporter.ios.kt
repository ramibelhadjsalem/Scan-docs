package com.scandoc.core.platform

import com.scandoc.domain.model.Page
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSData
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual class PlatformPdfExporter : com.scandoc.domain.platform.PdfExporter {

    actual override suspend fun export(pages: List<Page>, outputPath: String): String {
        val document = PDFDocument()

        pages.forEachIndexed { index, page ->
            runCatching {
                val imageBytes = FileSystem.SYSTEM.read(page.imagePath.toPath()) { readByteArray() }
                val uiImage = UIImage(data = imageBytes.toNSData()) ?: return@runCatching
                val pdfPage = PDFPage(image = uiImage) ?: return@runCatching
                document.insertPage(pdfPage, atIndex = index.toLong())
            }
        }

        val pdfData = document.dataRepresentation()
            ?: error("PDFDocument failed to generate data representation")

        val path = outputPath.toPath()
        path.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
        FileSystem.SYSTEM.write(path) {
            write(pdfData.toByteArray())
        }
        return outputPath
    }
}
