package com.scandoc.presentation.library

import com.scandoc.domain.model.Document

fun Document.hasOcrText(): Boolean =
    pages.any { page -> page.ocrResult?.fullText?.isNotBlank() == true }

fun documentPageSummary(pageCount: Int, hasOcrText: Boolean): String {
    val pageLabel = if (pageCount == 1) "page" else "pages"
    val status = if (hasOcrText) "OCR ready" else "scan saved"
    return "$pageCount $pageLabel · $status"
}

fun documentBadge(pageCount: Int, hasOcrText: Boolean): String =
    when {
        hasOcrText -> "OCR"
        pageCount > 1 -> "PDF"
        else -> "IMG"
    }
