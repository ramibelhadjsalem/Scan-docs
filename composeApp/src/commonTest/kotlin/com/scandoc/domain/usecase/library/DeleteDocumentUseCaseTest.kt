package com.scandoc.domain.usecase.library

import com.scandoc.domain.result.Outcome
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.fake.FakeImageRepository
import com.scandoc.testDocument
import com.scandoc.testPage
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
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
    fun `should delete page images when document has pages`() = runTest {
        val imagePath = "doc-001/page_0.jpg"
        fakeImageRepo.savedImages[imagePath] = ByteArray(10)
        fakeDocRepo.seed(testDocument(id = "doc-001", pages = listOf(testPage(imagePath = imagePath))))

        useCase("doc-001")

        assertFalse(fakeImageRepo.savedImages.containsKey(imagePath))
    }

    @Test
    fun `should return failure when repository fails`() = runTest {
        fakeDocRepo.seed(testDocument(id = "doc-001"))
        fakeDocRepo.shouldFail = true

        val result = useCase("doc-001")

        assertIs<Outcome.Failure>(result)
    }
}
