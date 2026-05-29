package com.scandoc.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Platform-specific camera preview composable.
 *
 * Android: embeds a CameraX [androidx.camera.view.PreviewView].
 * iOS: embeds a UIView backed by an AVCaptureVideoPreviewLayer.
 */
@Composable
expect fun CameraPreview(
    modifier: Modifier = Modifier,
)
