package com.scandoc.core.platform

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.scandoc.domain.model.Page
import java.io.File
import java.io.FileOutputStream

actual class PlatformPdfExporter : com.scandoc.domain.platform.PdfExporter {
    actual override suspend fun export(pages: List<Page>, outputPath: String): String {
        val file = File(outputPath)
        file.parentFile?.mkdirs()

        val pdf = PdfDocument()
        try {
            pages.ifEmpty { listOf(null) }.forEachIndexed { index, page ->
                val bitmap = page?.let { BitmapFactory.decodeFile(it.imagePath) }
                val width = bitmap?.width ?: 595
                val height = bitmap?.height ?: 842
                val pdfPage = pdf.startPage(
                    PdfDocument.PageInfo.Builder(width, height, index + 1).create(),
                )
                if (bitmap == null) {
                    pdfPage.canvas.drawText(
                        "ScanDoc page ${index + 1}",
                        48f,
                        96f,
                        Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 18f },
                    )
                } else {
                    pdfPage.canvas.drawBitmap(bitmap, 0f, 0f, null)
                    bitmap.recycle()
                }
                pdf.finishPage(pdfPage)
            }

            FileOutputStream(file).use { output ->
                pdf.writeTo(output)
            }
        } finally {
            pdf.close()
        }
        return outputPath
    }
}
