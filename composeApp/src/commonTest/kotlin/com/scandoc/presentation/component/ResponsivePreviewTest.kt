package com.scandoc.presentation.component

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResponsivePreviewTest {
    @Test
    fun fittedPreviewSizeScalesDownToAvailableHeight() {
        val size = fittedPreviewSize(
            maxWidth = 320f,
            maxHeight = 420f,
            preferredWidth = 310f,
            preferredHeight = 500f,
        )

        assertClose(expected = 260.4f, actual = size.width)
        assertEquals(expected = 420f, actual = size.height)
    }

    @Test
    fun fittedPreviewSizeDoesNotUpscalePastPreferredSize() {
        val size = fittedPreviewSize(
            maxWidth = 600f,
            maxHeight = 900f,
            preferredWidth = 310f,
            preferredHeight = 500f,
        )

        assertEquals(expected = 310f, actual = size.width)
        assertEquals(expected = 500f, actual = size.height)
    }

    @Test
    fun scanDocTouchTargetKeepsCompactControlsAccessible() {
        assertTrue(scanDocTouchTargetDp >= 48f)
        assertTrue(scanDocIconVisualDp < scanDocTouchTargetDp)
    }

    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(
            actual = abs(expected - actual) < 0.01f,
            message = "Expected $actual to be within 0.01 of $expected",
        )
    }
}
