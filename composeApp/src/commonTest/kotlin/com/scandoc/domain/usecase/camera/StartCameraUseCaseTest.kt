package com.scandoc.domain.usecase.camera

import com.scandoc.domain.result.Outcome
import com.scandoc.fake.FakeCameraController
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class StartCameraUseCaseTest {

    private val fakeController = FakeCameraController()
    private val useCase = StartCameraUseCase(fakeController)

    @Test
    fun `should start camera controller and return success`() = runTest {
        val result = useCase()

        assertIs<Outcome.Success<Unit>>(result)
        assertTrue(fakeController.isStarted)
    }
}
