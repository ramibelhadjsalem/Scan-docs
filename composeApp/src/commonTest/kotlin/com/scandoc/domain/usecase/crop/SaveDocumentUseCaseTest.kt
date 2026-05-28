package com.scandoc.domain.usecase.crop

import com.scandoc.domain.result.Outcome
import com.scandoc.fake.FakeDocumentRepository
import com.scandoc.fake.FakeImageRepository
import com.scandoc.fake.FakeOcrEngine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SaveDocumentUseCaseTest {

    private val fakeDocRepo = FakeDocumentRepository()
    private val fakeImageRepo = FakeImageRepository()
    private val fakeOcrEngine = FakeOcrEngine()
    private val useCase = SaveDocumentUseCase(fakeDocRepo, fakeImageRepo, fakeOcrEngine)

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
    fun `should save page with ocr result when ocr engine succeeds`() = runTest {
        fakeOcrEngine.resultToReturn = fakeOcrEngine.resultToReturn.copy(fullText = "Invoice total: 100")

        val result = useCase("Invoice", ByteArray(100))

        assertIs<Outcome.Success<*>>(result)
        assertEquals("Invoice total: 100", result.value.pages.first().ocrResult?.fullText)
    }

    @Test
    fun `should save page with null ocr result when ocr engine fails`() = runTest {
        fakeOcrEngine.shouldFail = true

        val result = useCase("Test", ByteArray(100))

        assertIs<Outcome.Success<*>>(result)
        assertNull(result.value.pages.first().ocrResult)
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
