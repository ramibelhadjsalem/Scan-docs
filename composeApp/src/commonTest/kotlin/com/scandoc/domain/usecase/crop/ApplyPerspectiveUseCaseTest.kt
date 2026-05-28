package com.scandoc.domain.usecase.crop

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Offset
import com.scandoc.fake.FakeImageProcessor
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ApplyPerspectiveUseCaseTest {

    private val fakeProcessor = FakeImageProcessor()
    private val useCase = ApplyPerspectiveUseCase(fakeProcessor)

    private val testCorners = DocumentCorners(
        topLeft = Offset(0.05f, 0.08f),
        topRight = Offset(0.92f, 0.07f),
        bottomRight = Offset(0.95f, 0.91f),
        bottomLeft = Offset(0.04f, 0.93f),
    )

    @Test
    fun `should return corrected bytes when perspective is applied successfully`() = runTest {
        val expected = ByteArray(300) { (it % 128).toByte() }
        fakeProcessor.processedBytes = expected

        val result = useCase(ByteArray(100), testCorners)

        assertIs<Outcome.Success<ByteArray>>(result)
        assertContentEquals(expected, result.value)
    }

    @Test
    fun `should pass image bytes to processor unchanged`() = runTest {
        val imageBytes = ByteArray(64) { it.toByte() }

        useCase(imageBytes, testCorners)

        assertEquals(imageBytes.toList(), fakeProcessor.lastPerspectiveBytes?.toList())
    }

    @Test
    fun `should return failure outcome when processor throws`() = runTest {
        fakeProcessor.shouldThrow = true

        val result = useCase(ByteArray(100), testCorners)

        assertIs<Outcome.Failure>(result)
    }
}
