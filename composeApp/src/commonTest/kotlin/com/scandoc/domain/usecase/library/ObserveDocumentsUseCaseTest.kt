package com.scandoc.domain.usecase.library

import app.cash.turbine.test
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.testDocument
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveDocumentsUseCaseTest {

    private val fakeRepo = FakeDocumentRepository()
    private val useCase = ObserveDocumentsUseCase(fakeRepo)

    @Test
    fun `should emit empty list when repository has no documents`() = runTest {
        useCase().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit documents when repository has documents`() = runTest {
        fakeRepo.seed(testDocument(name = "Invoice"), testDocument(id = "doc-002", name = "Receipt"))

        useCase().test {
            assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit updated list when document is added`() = runTest {
        useCase().test {
            assertTrue(awaitItem().isEmpty())

            fakeRepo.seed(testDocument())

            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
