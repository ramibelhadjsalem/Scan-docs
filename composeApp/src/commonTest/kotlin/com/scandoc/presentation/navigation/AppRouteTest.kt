package com.scandoc.presentation.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class AppRouteTest {
    @Test
    fun viewerRouteUsesDocumentIdSegment() {
        assertEquals(
            expected = "viewer/document-42",
            actual = AppRoute.Viewer("document-42").path,
        )
    }

    @Test
    fun cropRouteKeepsCapturedBytesInScreenState() {
        val bytes = byteArrayOf(1, 2, 3)
        val route = AppRoute.Crop(bytes)

        assertEquals(expected = "crop", actual = route.path)
        assertEquals(expected = 3, actual = route.imageBytes.size)
    }
}
