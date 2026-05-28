package com.scandoc.domain.usecase.camera

import com.scandoc.core.result.Outcome
import com.scandoc.fake.FakeCameraController
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertIs

class CaptureFrameUseCaseTest {

    private val fakeCamera = FakeCameraController()
    private val useCase = CaptureFrameUseCase(fakeCamera)

    @Test
    fun `should return image bytes when camera captures successfully`() = runTest {
        val expected = ByteArray(100) { it.toByte() }
        fakeCamera.captureResult = expected

        val result = useCase()

        assertIs<Outcome.Success<ByteArray>>(result)
        assertContentEquals(expected, result.value)
    }

    @Test
    fun `should return failure outcome when camera throws`() = runTest {
        fakeCamera.shouldThrow = true

        val result = useCase()

        assertIs<Outcome.Failure>(result)
    }
}
