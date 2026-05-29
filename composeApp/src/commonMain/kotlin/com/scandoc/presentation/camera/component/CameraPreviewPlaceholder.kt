package com.scandoc.presentation.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.scandoc.presentation.component.PaperPreview
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraPreviewPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = ScanDocDimens.cameraPreviewVerticalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.72f }
                .padding(horizontal = ScanDocDimens.space3xl),
        ) {
            PaperPreview(modifier = Modifier.fillMaxSize())
        }
    }
}
