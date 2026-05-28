package com.scandoc.fake

import com.scandoc.core.platform.PdfExportAccess
import com.scandoc.domain.model.Page

class FakePdfExportAccess : PdfExportAccess {
    var shouldThrow = false
    var lastPages: List<Page>? = null
    var lastOutputPath: String? = null

    override suspend fun export(pages: List<Page>, outputPath: String): String {
        lastPages = pages
        lastOutputPath = outputPath
        if (shouldThrow) throw IllegalStateException("PDF export failure")
        return outputPath
    }
}
