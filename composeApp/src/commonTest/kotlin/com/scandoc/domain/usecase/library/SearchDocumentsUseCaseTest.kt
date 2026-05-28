package com.scandoc.domain.usecase.library

import app.cash.turbine.test
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.testDocument
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchDocumentsUseCaseTest {

    private val fakeRepo = FakeDocumentRepository()
    private val useCase = SearchDocumentsUseCase(fakeRepo)

    @Test
    fun `should return matching documents when query matches name`() = runTest {
        fakeRepo.seed(
            testDocument(id = "1", name = "Invoice March"),
            testDocument(id = "2", name = "Receipt April"),
            testDocument(id = "3", name = "Invoice April"),
        )

        useCase("Invoice").test {
            val results = awaitItem()
            assertEquals(2, results.size)
            assertTrue(results.all { it.name.contains("Invoice", ignoreCase = true) })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return empty list when query matches nothing`() = runTest {
        fakeRepo.seed(testDocument(name = "Invoice"))

        useCase("XYZ").test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should be case insensitive when searching`() = runTest {
        fakeRepo.seed(testDocument(name = "INVOICE"))

        useCase("invoice").test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
