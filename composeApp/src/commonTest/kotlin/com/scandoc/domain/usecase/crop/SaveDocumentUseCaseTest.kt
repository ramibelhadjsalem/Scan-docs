package com.scandoc.domain.usecase.crop

import com.scandoc.domain.result.Outcome
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.fake.FakeImageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SaveDocumentUseCaseTest {

    private val fakeDocRepo = FakeDocumentRepository()
    private val fakeImageRepo = FakeImageRepository()
    private val useCase = SaveDocumentUseCase(fakeDocRepo, fakeImageRepo)

    @Test
    fun `should persist document and return it when save succeeds`() = runTest {
        val result = useCase("Invoice", ByteArray(100))

        assertIs<Outcome.Success<*>>(result)
        assertEquals("Invoice", result.value.name)
        assertEquals(1, result.value.pages.size)
        assertNotNull(fakeDocRepo.getById(result.value.id))
    }

    @Test
    fun `should save image bytes before persisting document`() = runTest {
        val imageBytes = ByteArray(50) { it.toByte() }

        val result = useCase("Test", imageBytes)

        assertIs<Outcome.Success<*>>(result)
        val imagePath = result.value.pages.first().imagePath
        assertTrue(fakeImageRepo.savedImages.containsKey(imagePath))
    }

    @Test
    fun `should return failure and not create document when image save fails`() = runTest {
        fakeImageRepo.shouldFail = true

        val result = useCase("Test", ByteArray(10))

        assertIs<Outcome.Failure>(result)
        assertEquals(0, fakeDocRepo.observeAll().first().size)
    }

    @Test
    fun `should return failure when document repository save fails`() = runTest {
        fakeDocRepo.shouldFail = true

        val result = useCase("Test", ByteArray(10))

        assertIs<Outcome.Failure>(result)
    }
}
