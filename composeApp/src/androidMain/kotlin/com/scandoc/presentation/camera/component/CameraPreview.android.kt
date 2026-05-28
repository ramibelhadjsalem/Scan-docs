package com.scandoc.presentation.camera.component

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.scandoc.core.platform.PlatformCameraController
import com.scandoc.domain.platform.CameraController
import org.koin.compose.koinInject

@Composable
actual fun CameraPreview(modifier: Modifier = Modifier) {
    val controller = koinInject<CameraController>() as PlatformCameraController
    AndroidView(
        factory = { context ->
            PreviewView(context).also { previewView ->
                controller.previewSurfaceProvider = previewView.surfaceProvider
            }
        },
        modifier = modifier,
    )
}
