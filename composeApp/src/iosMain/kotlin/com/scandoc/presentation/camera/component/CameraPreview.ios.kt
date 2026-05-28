package com.scandoc.presentation.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// iOS camera preview — AVFoundation integration (Phase 8)
@Composable
actual fun CameraPreview(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.Black))
}
