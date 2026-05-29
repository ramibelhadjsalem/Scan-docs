package com.scandoc.presentation.library

import kotlin.test.Test
import kotlin.test.assertEquals

class DocumentUiTextTest {
    @Test
    fun pageSummaryShowsOcrReadyWhenAnyPageHasText() {
        assertEquals(
            expected = "3 pages · OCR ready",
            actual = documentPageSummary(pageCount = 3, hasOcrText = true),
        )
    }

    @Test
    fun pageSummaryUsesSingularPageLabel() {
        assertEquals(
            expected = "1 page · scan saved",
            actual = documentPageSummary(pageCount = 1, hasOcrText = false),
        )
    }
}
