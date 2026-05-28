package com.scandoc.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Offset
import com.scandoc.presentation.camera.component.DetectionOverlay
import com.scandoc.presentation.camera.component.ModeSwitcher
import com.scandoc.presentation.camera.component.ShutterButton
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CameraScreen(
    state: CameraState,
    onIntent: (CameraIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // Camera viewfinder placeholder with document detection overlay
        Box(modifier = Modifier.fillMaxSize()) {
            DetectionOverlay(
                corners = state.detectedCorners,
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Top bar
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(ScanDocDimens.spaceMd),
        ) {
            Icon(
                imageVector = Icons.Default.DocumentScanner,
                contentDescription = "Document Scanner",
                tint = ScanDocColors.Text,
                modifier = Modifier.size(ScanDocDimens.iconSizeLg),
            )
        }

        // Error snackbar
        if (state.error != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = ScanDocDimens.space6xl)
                    .padding(horizontal = ScanDocDimens.spaceMd),
            ) {
                Surface(
                    shape = ScanDocShapes.Medium,
                    color = ScanDocColors.Danger,
                ) {
                    Text(
                        text = state.error,
                        color = ScanDocColors.Text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            horizontal = ScanDocDimens.spaceMd,
                            vertical = ScanDocDimens.spaceXs,
                        ),
                    )
                }
            }
        }

        // Bottom control bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(ScanDocColors.Ink.copy(alpha = 0.7f))
                .padding(ScanDocDimens.spaceMd),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                ModeSwitcher(
                    isFlashOn = state.isFlashOn,
                    isAutoCapture = state.isAutoCapture,
                    onToggleFlash = { onIntent(CameraIntent.ToggleFlash) },
                    onToggleAutoCapture = { onIntent(CameraIntent.ToggleAutoCapture(it)) },
                )

                ShutterButton(
                    onClick = { onIntent(CameraIntent.Capture) },
                    countdown = state.captureCountdown,
                    enabled = state.isActive,
                )

                // Balance spacer matching ModeSwitcher width
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun CameraScreenPreview() {
    ScanDocTheme {
        CameraScreen(
            state = CameraState(
                isActive = true,
                isFlashOn = false,
                isAutoCapture = true,
                detectedCorners = DocumentCorners(
                    topLeft = Offset(0.1f, 0.05f),
                    topRight = Offset(0.9f, 0.08f),
                    bottomRight = Offset(0.88f, 0.95f),
                    bottomLeft = Offset(0.12f, 0.92f),
                ),
            ),
            onIntent = {},
        )
    }
}
