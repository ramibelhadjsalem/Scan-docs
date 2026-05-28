package com.scandoc.domain.platform

import com.scandoc.domain.model.Page

interface PdfExporter {
    suspend fun export(pages: List<Page>, outputPath: String): String
}
