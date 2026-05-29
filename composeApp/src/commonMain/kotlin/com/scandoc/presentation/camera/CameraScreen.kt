package com.scandoc.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.presentation.camera.component.CameraTopBar
import com.scandoc.core.platform.CameraPreview
import com.scandoc.presentation.camera.component.CameraStatusBadge
import com.scandoc.presentation.camera.component.CameraStubIcon
import com.scandoc.presentation.camera.component.DetectionOverlay
import com.scandoc.presentation.camera.component.FeatureStrip
import com.scandoc.presentation.camera.component.ModeSwitcher
import com.scandoc.presentation.camera.component.ShutterButton
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraScreen(
    state: CameraState,
    onIntent: (CameraIntent) -> Unit,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScanDocColors.Ink),
    ) {
        CameraPreview(Modifier.fillMaxSize())

        if (state.detectedCorners != null) {
            DetectionOverlay(
                corners = state.detectedCorners,
                modifier = Modifier.fillMaxSize(),
            )
        }

        CameraTopBar(
            isFlashOn = state.isFlashOn,
            isHdMode = state.isHdMode,
            onClose = { onClose?.invoke() ?: onIntent(CameraIntent.StopCamera) },
            onToggleFlash = { onIntent(CameraIntent.ToggleFlash) },
            onToggleHd = { onIntent(CameraIntent.ToggleHd) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = ScanDocDimens.spaceMd),
        )

        CameraBottomControls(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        if (!state.isActive || state.error != null) {
            CameraStatusBadge(
                text = state.error ?: "Camera paused",
                isError = state.error != null,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun CameraBottomControls(
    state: CameraState,
    onIntent: (CameraIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = ScanDocDimens.cameraBottomControlsPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        ModeSwitcher(
            selected = state.selectedMode,
            onSelect = { onIntent(CameraIntent.SelectMode(it)) },
        )
        FeatureStrip(
            features = CameraFeature.all,
            selected = state.selectedFeature,
            onSelect = { onIntent(CameraIntent.SelectFeature(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ScanDocDimens.cameraBottomSidePadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CameraStubIcon(imageVector = Icons.Outlined.GridView, contentDescription = "Gallery")
            ShutterButton(onClick = { onIntent(CameraIntent.Capture) })
            CameraStubIcon(imageVector = Icons.Outlined.ImageSearch, contentDescription = "Import")
        }
    }
}
