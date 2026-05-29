package com.scandoc.presentation.camera.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CameraTopBar(
    isFlashOn: Boolean,
    isHdMode: Boolean,
    onClose: () -> Unit,
    onToggleFlash: () -> Unit,
    onToggleHd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .height(ScanDocDimens.cameraTopBarHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CameraIconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close camera",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            CameraIconButton(onClick = onToggleFlash) {
                Icon(
                    imageVector = if (isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                    contentDescription = "Toggle flash",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            HdBadge(enabled = isHdMode, onClick = onToggleHd)
            CameraIconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "Filters",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            CameraIconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "More camera options",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun CameraIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(ScanDocDimens.cameraControlButtonSize)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun HdBadge(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(ScanDocDimens.modePillHeight)
            .widthIn(min = ScanDocDimens.cameraBadgeMinWidth)
            .background(
                color = if (enabled) ScanDocColors.TealDim else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(ScanDocDimens.modePillSelectedCornerRadius),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "HD",
            color = if (enabled) ScanDocColors.Teal else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}
