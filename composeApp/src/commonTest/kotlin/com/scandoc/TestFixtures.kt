package com.scandoc

import com.scandoc.domain.model.Document
import com.scandoc.domain.model.OcrResult
import com.scandoc.domain.model.Page
import kotlinx.datetime.Instant

fun testDocument(
    id: String = "doc-001",
    name: String = "Test Document",
    pages: List<Page> = emptyList(),
    tags: List<String> = emptyList(),
): Document = Document(
    id = id,
    name = name,
    createdAt = Instant.fromEpochMilliseconds(0),
    updatedAt = Instant.fromEpochMilliseconds(0),
    pages = pages,
    thumbnailPath = null,
    tags = tags,
)

fun testPage(
    id: String = "page-001",
    orderIndex: Int = 0,
    imagePath: String = "/fake/path/page.jpg",
    ocrResult: OcrResult? = null,
): Page = Page(
    id = id,
    orderIndex = orderIndex,
    imagePath = imagePath,
    ocrResult = ocrResult,
    width = 1080,
    height = 1920,
)
