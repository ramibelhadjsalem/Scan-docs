package com.scandoc.domain.usecase.library

import com.scandoc.core.result.Outcome
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.fake.FakeImageRepository
import com.scandoc.testDocument
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

class DeleteDocumentUseCaseTest {

    private val fakeDocRepo = FakeDocumentRepository()
    private val fakeImageRepo = FakeImageRepository()
    private val useCase = DeleteDocumentUseCase(fakeDocRepo, fakeImageRepo)

    @Test
    fun `should return success and remove document when delete called`() = runTest {
        fakeDocRepo.seed(testDocument(id = "doc-001"))

        val result = useCase("doc-001")

        assertIs<Outcome.Success<Unit>>(result)
        assertNull(fakeDocRepo.getById("doc-001"))
    }

    @Test
    fun `should return failure when repository fails`() = runTest {
        fakeDocRepo.seed(testDocument(id = "doc-001"))
        fakeDocRepo.shouldFail = true

        val result = useCase("doc-001")

        assertIs<Outcome.Failure>(result)
    }
}
