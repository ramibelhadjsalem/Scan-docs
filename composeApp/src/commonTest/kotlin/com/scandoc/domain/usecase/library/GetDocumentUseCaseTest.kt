package com.scandoc.domain.usecase.library

import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.testDocument
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetDocumentUseCaseTest {

    private val fakeRepo = FakeDocumentRepository()
    private val useCase = GetDocumentUseCase(fakeRepo)

    @Test
    fun `should return document by id when it exists`() = runTest {
        fakeRepo.seed(
            testDocument(id = "doc-1", name = "Invoice"),
            testDocument(id = "doc-2", name = "Receipt"),
        )

        val result = useCase("doc-2")

        assertEquals("Receipt", result?.name)
    }

    @Test
    fun `should return null when document does not exist`() = runTest {
        fakeRepo.seed(testDocument(id = "doc-1"))

        val result = useCase("missing")

        assertNull(result)
    }
}
