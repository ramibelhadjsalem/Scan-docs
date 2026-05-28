package com.scandoc.presentation.camera.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Live camera viewfinder.
 * Android: CameraX PreviewView bound to PlatformCameraController.
 * iOS: AVCaptureVideoPreviewLayer (Phase 8).
 */
@Composable
expect fun CameraPreview(modifier: Modifier = Modifier)
