package com.scandoc.domain.usecase.camera

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Offset
import com.scandoc.fake.FakeImageProcessor
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class DetectEdgesUseCaseTest {

    private val fakeProcessor = FakeImageProcessor()
    private val useCase = DetectEdgesUseCase(fakeProcessor)

    @Test
    fun `should return detected corners when processor finds document edges`() = runTest {
        val corners = DocumentCorners(
            topLeft = Offset(0.1f, 0.1f),
            topRight = Offset(0.9f, 0.1f),
            bottomRight = Offset(0.9f, 0.9f),
            bottomLeft = Offset(0.1f, 0.9f),
        )
        fakeProcessor.cornersToReturn = corners

        val result = useCase(ByteArray(100))

        assertIs<Outcome.Success<DocumentCorners?>>(result)
        assertEquals(corners, result.value)
    }

    @Test
    fun `should return null corners when no document is detected`() = runTest {
        fakeProcessor.cornersToReturn = null

        val result = useCase(ByteArray(100))

        assertIs<Outcome.Success<DocumentCorners?>>(result)
        assertNull(result.value)
    }

    @Test
    fun `should return failure outcome when processor throws`() = runTest {
        fakeProcessor.shouldThrow = true

        val result = useCase(ByteArray(100))

        assertIs<Outcome.Failure>(result)
    }

    @Test
    fun `should pass image bytes to processor unchanged`() = runTest {
        val imageBytes = ByteArray(50) { (it * 2).toByte() }

        useCase(imageBytes)

        assertEquals(imageBytes.toList(), fakeProcessor.lastDetectBytes?.toList())
    }
}
