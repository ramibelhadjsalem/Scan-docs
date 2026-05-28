package com.scandoc.data.local.mapper

import com.scandoc.db.Document as DocumentRow
import com.scandoc.db.Page as PageRow
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DocumentMapperTest {

    @Test
    fun `should map document and page rows to domain document`() {
        val document = DocumentRow(
            id = "doc-1",
            name = "Invoice",
            createdAt = 1_000,
            updatedAt = 2_000,
            pageCount = 1,
            thumbnailPath = "/thumb.jpg",
            tags = "tax,work",
        )
        val page = PageRow(
            id = "page-1",
            documentId = "doc-1",
            orderIndex = 2,
            imagePath = "/page.jpg",
            ocrText = "Invoice total",
            ocrConfidence = 0.9,
            width = 1080,
            height = 1920,
        )

        val result = document.toDomain(pages = listOf(page))

        assertEquals("doc-1", result.id)
        assertEquals("Invoice", result.name)
        assertEquals(Instant.fromEpochMilliseconds(1_000), result.createdAt)
        assertEquals(Instant.fromEpochMilliseconds(2_000), result.updatedAt)
        assertEquals("/thumb.jpg", result.thumbnailPath)
        assertEquals(listOf("tax", "work"), result.tags)
        assertEquals("page-1", result.pages.single().id)
        assertEquals(2, result.pages.single().orderIndex)
        assertEquals("Invoice total", result.pages.single().ocrResult?.fullText)
        assertEquals(0.9f, result.pages.single().ocrResult?.confidence)
    }

    @Test
    fun `should map missing ocr fields to null ocr result`() {
        val page = PageRow(
            id = "page-1",
            documentId = "doc-1",
            orderIndex = 0,
            imagePath = "/page.jpg",
            ocrText = null,
            ocrConfidence = null,
            width = 1080,
            height = 1920,
        )

        val result = page.toDomain()

        assertNull(result.ocrResult)
    }

    @Test
    fun `should encode and decode tags for storage`() {
        assertEquals("tax,work", listOf("tax", "work").toStorageTags())
        assertEquals(listOf("tax", "work"), "tax,work".toDomainTags())
        assertEquals(emptyList(), "".toDomainTags())
    }
}
