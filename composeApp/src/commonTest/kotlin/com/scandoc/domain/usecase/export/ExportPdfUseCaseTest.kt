package com.scandoc.domain.usecase.export

import com.scandoc.core.result.Outcome
import com.scandoc.fake.FakeFileSystemAccess
import com.scandoc.fake.FakePdfExportAccess
import com.scandoc.testDocument
import com.scandoc.testPage
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ExportPdfUseCaseTest {

    private val fakePdfExporter = FakePdfExportAccess()
    private val fakeFileSystem = FakeFileSystemAccess(documentsDir = "/docs")
    private val useCase = ExportPdfUseCase(fakePdfExporter, fakeFileSystem)

    @Test
    fun `should return output path when export succeeds`() = runTest {
        val document = testDocument(name = "Invoice")

        val result = useCase(document)

        assertIs<Outcome.Success<String>>(result)
        assertEquals("/docs/Invoice.pdf", result.value)
    }

    @Test
    fun `should pass document pages to exporter`() = runTest {
        val pages = listOf(testPage(id = "p1"), testPage(id = "p2", orderIndex = 1))
        val document = testDocument(pages = pages)

        useCase(document)

        assertEquals(pages, fakePdfExporter.lastPages)
    }

    @Test
    fun `should build output path from documents dir and document name`() = runTest {
        val document = testDocument(name = "My Scan")

        useCase(document)

        assertEquals("/docs/My Scan.pdf", fakePdfExporter.lastOutputPath)
    }

    @Test
    fun `should return failure outcome when exporter throws`() = runTest {
        fakePdfExporter.shouldThrow = true

        val result = useCase(testDocument())

        assertIs<Outcome.Failure>(result)
    }
}
