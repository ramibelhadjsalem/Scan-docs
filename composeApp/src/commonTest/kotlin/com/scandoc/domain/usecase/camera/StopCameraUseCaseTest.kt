package com.scandoc.domain.usecase.camera

import com.scandoc.domain.result.Outcome
import com.scandoc.fake.FakeCameraController
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs

class StopCameraUseCaseTest {

    private val fakeController = FakeCameraController()
    private val useCase = StopCameraUseCase(fakeController)

    @Test
    fun `should stop camera controller and return success`() = runTest {
        fakeController.isStarted = true

        val result = useCase()

        assertIs<Outcome.Success<Unit>>(result)
        assertFalse(fakeController.isStarted)
    }
}
