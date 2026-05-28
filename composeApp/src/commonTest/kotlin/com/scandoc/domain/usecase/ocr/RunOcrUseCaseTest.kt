package com.scandoc.domain.usecase.ocr

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.OcrResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RunOcrUseCaseTest {

    private val fakeOcrEngine = FakeOcrEngine()
    private val useCase = RunOcrUseCase(fakeOcrEngine)

    @Test
    fun `should return ocr result when engine succeeds`() = runTest {
        fakeOcrEngine.resultToReturn = OcrResult(
            fullText = "Hello World",
            blocks = emptyList(),
            confidence = 0.95f,
            language = "en",
        )

        val result = useCase(ByteArray(100))

        assertIs<Outcome.Success<OcrResult>>(result)
        assertEquals("Hello World", result.value.fullText)
        assertEquals(0.95f, result.value.confidence)
    }

    @Test
    fun `should return failure outcome when engine throws`() = runTest {
        fakeOcrEngine.shouldThrow = true

        val result = useCase(ByteArray(100))

        assertIs<Outcome.Failure>(result)
    }

    @Test
    fun `should return failure when image bytes are empty`() = runTest {
        fakeOcrEngine.shouldThrow = true

        val result = useCase(ByteArray(0))

        assertIs<Outcome.Failure>(result)
    }
}

private class FakeOcrEngine : com.scandoc.core.platform.OcrEngine() {
    var resultToReturn: OcrResult = OcrResult("", emptyList(), 0f, null)
    var shouldThrow = false

    override suspend fun recognize(imageBytes: ByteArray): OcrResult {
        if (shouldThrow) throw IllegalStateException("OCR engine failure")
        return resultToReturn
    }
}
