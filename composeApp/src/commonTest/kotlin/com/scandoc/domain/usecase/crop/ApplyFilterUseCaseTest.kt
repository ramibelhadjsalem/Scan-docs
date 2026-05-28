package com.scandoc.domain.usecase.crop

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.Filter
import com.scandoc.fake.FakeImageProcessor
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ApplyFilterUseCaseTest {

    private val fakeProcessor = FakeImageProcessor()
    private val useCase = ApplyFilterUseCase(fakeProcessor)

    @Test
    fun `should return processed bytes when filter is applied successfully`() = runTest {
        val expected = ByteArray(200) { it.toByte() }
        fakeProcessor.processedBytes = expected

        val result = useCase(ByteArray(100), Filter.BlackWhite)

        assertIs<Outcome.Success<ByteArray>>(result)
        assertContentEquals(expected, result.value)
    }

    @Test
    fun `should pass the selected filter to processor`() = runTest {
        useCase(ByteArray(100), Filter.Grayscale)

        assertEquals(Filter.Grayscale, fakeProcessor.lastFilterApplied)
    }

    @Test
    fun `should return failure outcome when processor throws`() = runTest {
        fakeProcessor.shouldThrow = true

        val result = useCase(ByteArray(100), Filter.Auto)

        assertIs<Outcome.Failure>(result)
    }

    @Test
    fun `should handle all filter variants without error`() = runTest {
        Filter.entries.forEach { filter ->
            fakeProcessor.shouldThrow = false
            val result = useCase(ByteArray(10), filter)
            assertIs<Outcome.Success<ByteArray>>(result)
        }
    }
}
