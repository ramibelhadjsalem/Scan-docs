package com.scandoc.presentation.crop.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.presentation.component.DetectionCorners
import com.scandoc.presentation.component.PaperPreview
import com.scandoc.presentation.component.SquareGrid
import com.scandoc.presentation.component.fittedPreviewSize
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CropCanvas(
    corners: DocumentCorners?,
    modifier: Modifier = Modifier,
) {
    val cornerColor = MaterialTheme.colorScheme.primary.copy(alpha = if (corners == null) 0.82f else 1f)
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = ScanDocColors.Text.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
            )
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.78f)),
        contentAlignment = Alignment.Center,
    ) {
        SquareGrid(modifier = Modifier.matchParentSize())
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScanDocDimens.spaceLg),
            contentAlignment = Alignment.Center,
        ) {
            val preview = fittedPreviewSize(
                maxWidth = maxWidth.value,
                maxHeight = maxHeight.value,
                preferredWidth = 240f,
                preferredHeight = 330f,
            )
            val overlay = fittedPreviewSize(
                maxWidth = maxWidth.value,
                maxHeight = maxHeight.value,
                preferredWidth = 276f,
                preferredHeight = 366f,
            )
            PaperPreview(
                modifier = Modifier
                    .width(preview.width.dp)
                    .height(preview.height.dp)
                    .graphicsLayer { rotationZ = -2f },
            )
            DetectionCorners(
                modifier = Modifier
                    .width(overlay.width.dp)
                    .height(overlay.height.dp)
                    .padding(ScanDocDimens.spaceSm)
                    .graphicsLayer { rotationZ = -2f },
                color = cornerColor,
            )
        }
    }
}
