package com.scandoc.domain.usecase.export

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.ExportFormat
import com.scandoc.fake.FakeFileSystemAccess
import com.scandoc.testDocument
import com.scandoc.testPage
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ExportImagesUseCaseTest {

    private val fakeFileSystem = FakeFileSystemAccess(documentsDir = "/docs")
    private val useCase = ExportImagesUseCase(fakeFileSystem)

    @Test
    fun `should return one path per page when exporting as jpeg`() = runTest {
        val pages = listOf(testPage(orderIndex = 0), testPage(id = "p2", orderIndex = 1))
        val document = testDocument(id = "doc-abc", pages = pages)

        val result = useCase(document, ExportFormat.JpegImages)

        assertIs<Outcome.Success<List<String>>>(result)
        assertEquals(2, result.value.size)
    }

    @Test
    fun `should use jpg extension for jpeg format`() = runTest {
        val document = testDocument(id = "doc-abc", pages = listOf(testPage(orderIndex = 0)))

        val result = useCase(document, ExportFormat.JpegImages)

        assertIs<Outcome.Success<List<String>>>(result)
        assertEquals("/docs/doc-abc_page0.jpg", result.value.first())
    }

    @Test
    fun `should use png extension for png format`() = runTest {
        val document = testDocument(id = "doc-abc", pages = listOf(testPage(orderIndex = 0)))

        val result = useCase(document, ExportFormat.PngImages)

        assertIs<Outcome.Success<List<String>>>(result)
        assertEquals("/docs/doc-abc_page0.png", result.value.first())
    }

    @Test
    fun `should return empty list when document has no pages`() = runTest {
        val document = testDocument(pages = emptyList())

        val result = useCase(document, ExportFormat.JpegImages)

        assertIs<Outcome.Success<List<String>>>(result)
        assertEquals(emptyList(), result.value)
    }

    @Test
    fun `should include page order index in each path`() = runTest {
        val pages = listOf(
            testPage(id = "p0", orderIndex = 0),
            testPage(id = "p1", orderIndex = 1),
            testPage(id = "p2", orderIndex = 2),
        )
        val document = testDocument(id = "doc-xyz", pages = pages)

        val result = useCase(document, ExportFormat.JpegImages)

        assertIs<Outcome.Success<List<String>>>(result)
        assertEquals("/docs/doc-xyz_page0.jpg", result.value[0])
        assertEquals("/docs/doc-xyz_page1.jpg", result.value[1])
        assertEquals("/docs/doc-xyz_page2.jpg", result.value[2])
    }
}
