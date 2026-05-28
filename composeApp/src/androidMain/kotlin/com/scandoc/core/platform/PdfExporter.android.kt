package com.scandoc.core.platform

import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import com.scandoc.domain.model.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class PdfExporter {

    actual suspend fun export(pages: List<Page>, outputPath: String): String =
        withContext(Dispatchers.IO) {
            val pdfDocument = PdfDocument()
            pages.forEach { page ->
                val pageInfo = PdfDocument.PageInfo.Builder(
                    page.width,
                    page.height,
                    page.orderIndex + 1,
                ).create()
                val pdfPage = pdfDocument.startPage(pageInfo)
                val bitmap = BitmapFactory.decodeFile(page.imagePath)
                if (bitmap != null) {
                    pdfPage.canvas.drawBitmap(bitmap, 0f, 0f, null)
                    bitmap.recycle()
                }
                pdfDocument.finishPage(pdfPage)
            }

            val outputFile = File(outputPath)
            outputFile.parentFile?.mkdirs()
            outputFile.outputStream().use { pdfDocument.writeTo(it) }
            pdfDocument.close()
            outputPath
        }
}
