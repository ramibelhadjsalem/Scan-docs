package com.scandoc.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.presentation.camera.component.DetectionOverlay
import com.scandoc.presentation.camera.component.ModeSwitcher
import com.scandoc.presentation.camera.component.ShutterButton
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraScreen(
    state: CameraState,
    onIntent: (CameraIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScanDocDimens.spaceLg)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (state.isActive) "Camera preview pending platform view" else "Camera paused",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            DetectionOverlay(corners = state.detectedCorners)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(ScanDocDimens.spaceLg),
            verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ModeSwitcher(
                    autoCapture = state.isAutoCapture,
                    onAutoCaptureChange = { onIntent(CameraIntent.ToggleAutoCapture(it)) },
                )
                ScanDocButton(
                    text = if (state.isFlashOn) "Flash on" else "Flash off",
                    onClick = { onIntent(CameraIntent.ToggleFlash) },
                )
            }
            ShutterButton(onClick = { onIntent(CameraIntent.Capture) })
        }
    }
}
