package com.scandoc.core.platform

import androidx.camera.view.PreviewView
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraPreviewConfigurationTest {

    @Test
    fun cameraPreviewUsesComposeCompatibleImplementationMode() {
        assertEquals(
            PreviewView.ImplementationMode.COMPATIBLE,
            cameraPreviewImplementationMode(),
        )
    }
}
