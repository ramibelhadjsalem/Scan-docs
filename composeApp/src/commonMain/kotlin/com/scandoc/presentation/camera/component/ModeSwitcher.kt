package com.scandoc.presentation.camera.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.camera.CaptureMode
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ModeSwitcher(
    selected: CaptureMode,
    onSelect: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedOffset by animateDpAsState(
        targetValue = if (selected == CaptureMode.Single) ScanDocDimens.spaceXxs else ScanDocDimens.modePillSegmentWidth,
        label = "capture_mode_offset",
    )

    Box(
        modifier = modifier
            .width(ScanDocDimens.modePillWidth)
            .height(ScanDocDimens.modePillHeight)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                shape = RoundedCornerShape(ScanDocDimens.modePillCornerRadius),
            ),
    ) {
        Box(
            modifier = Modifier
                .offset(x = selectedOffset)
                .width(ScanDocDimens.modePillSegmentWidth)
                .fillMaxHeight()
                .shadow(
                    elevation = ScanDocDimens.spaceXxs,
                    shape = RoundedCornerShape(ScanDocDimens.modePillSelectedCornerRadius),
                )
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(ScanDocDimens.modePillSelectedCornerRadius),
                ),
        )

        Row {
            ModeOption(
                mode = CaptureMode.Single,
                selected = selected == CaptureMode.Single,
                onSelect = onSelect,
            )
            ModeOption(
                mode = CaptureMode.Lot,
                selected = selected == CaptureMode.Lot,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun ModeOption(
    mode: CaptureMode,
    selected: Boolean,
    onSelect: (CaptureMode) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(ScanDocDimens.modePillSegmentWidth)
            .height(ScanDocDimens.modePillHeight)
            .clickable { onSelect(mode) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = mode.label,
            color = if (selected) MaterialTheme.colorScheme.surface else ScanDocColors.Text2,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        )
    }
}
