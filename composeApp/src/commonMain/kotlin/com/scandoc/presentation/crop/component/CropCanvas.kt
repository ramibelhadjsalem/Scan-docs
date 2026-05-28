package com.scandoc.presentation.crop.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.scandoc.domain.model.Offset as DomainOffset

private fun pixelToNorm(px: Float, py: Float, w: Float, h: Float) = DomainOffset(px / w, py / h)

private fun normToPixel(norm: DomainOffset, w: Float, h: Float) = Offset(norm.x * w, norm.y * h)

@Composable
fun CropCanvas(
    imageBytes: ByteArray,
    corners: DocumentCorners,
    onCornersChanged: (DocumentCorners) -> Unit,
    modifier: Modifier = Modifier,
) {
    val localCorners = remember { mutableStateOf(corners) }

    LaunchedEffect(corners) {
        localCorners.value = corners
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val canvasWidth = constraints.maxWidth.toFloat()
        val canvasHeight = constraints.maxHeight.toFloat()

        // Image placeholder
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(ScanDocColors.Ink3),
        ) {
            Text(
                text = "Document Preview",
                color = ScanDocColors.Text3,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Quadrilateral outline overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = localCorners.value
            val tl = normToPixel(c.topLeft, canvasWidth, canvasHeight)
            val tr = normToPixel(c.topRight, canvasWidth, canvasHeight)
            val br = normToPixel(c.bottomRight, canvasWidth, canvasHeight)
            val bl = normToPixel(c.bottomLeft, canvasWidth, canvasHeight)
            val strokeWidth = 2.dp.toPx()
            val stroke = Stroke(width = strokeWidth)

            drawLine(
                color = ScanDocColors.Signal,
                start = tl,
                end = tr,
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = ScanDocColors.Signal,
                start = tr,
                end = br,
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = ScanDocColors.Signal,
                start = br,
                end = bl,
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = ScanDocColors.Signal,
                start = bl,
                end = tl,
                strokeWidth = strokeWidth,
            )
        }

        // Draggable corner handles
        val c = localCorners.value

        DraggableCorner(
            offset = normToPixel(c.topLeft, canvasWidth, canvasHeight),
            onDrag = { delta ->
                val current = localCorners.value
                val newCorner = pixelToNorm(
                    normToPixel(current.topLeft, canvasWidth, canvasHeight).x + delta.x,
                    normToPixel(current.topLeft, canvasWidth, canvasHeight).y + delta.y,
                    canvasWidth,
                    canvasHeight,
                )
                val updated = current.copy(topLeft = newCorner)
                localCorners.value = updated
                onCornersChanged(updated)
            },
        )

        DraggableCorner(
            offset = normToPixel(c.topRight, canvasWidth, canvasHeight),
            onDrag = { delta ->
                val current = localCorners.value
                val newCorner = pixelToNorm(
                    normToPixel(current.topRight, canvasWidth, canvasHeight).x + delta.x,
                    normToPixel(current.topRight, canvasWidth, canvasHeight).y + delta.y,
                    canvasWidth,
                    canvasHeight,
                )
                val updated = current.copy(topRight = newCorner)
                localCorners.value = updated
                onCornersChanged(updated)
            },
        )

        DraggableCorner(
            offset = normToPixel(c.bottomRight, canvasWidth, canvasHeight),
            onDrag = { delta ->
                val current = localCorners.value
                val newCorner = pixelToNorm(
                    normToPixel(current.bottomRight, canvasWidth, canvasHeight).x + delta.x,
                    normToPixel(current.bottomRight, canvasWidth, canvasHeight).y + delta.y,
                    canvasWidth,
                    canvasHeight,
                )
                val updated = current.copy(bottomRight = newCorner)
                localCorners.value = updated
                onCornersChanged(updated)
            },
        )

        DraggableCorner(
            offset = normToPixel(c.bottomLeft, canvasWidth, canvasHeight),
            onDrag = { delta ->
                val current = localCorners.value
                val newCorner = pixelToNorm(
                    normToPixel(current.bottomLeft, canvasWidth, canvasHeight).x + delta.x,
                    normToPixel(current.bottomLeft, canvasWidth, canvasHeight).y + delta.y,
                    canvasWidth,
                    canvasHeight,
                )
                val updated = current.copy(bottomLeft = newCorner)
                localCorners.value = updated
                onCornersChanged(updated)
            },
        )
    }
}

private fun defaultPreviewCorners() = DocumentCorners(
    topLeft = DomainOffset(0.1f, 0.05f),
    topRight = DomainOffset(0.9f, 0.08f),
    bottomRight = DomainOffset(0.88f, 0.95f),
    bottomLeft = DomainOffset(0.12f, 0.92f),
)

@Preview
@Composable
private fun CropCanvasDarkPreview() {
    ScanDocTheme {
        CropCanvas(
            imageBytes = ByteArray(0),
            corners = defaultPreviewCorners(),
            onCornersChanged = {},
        )
    }
}

@Preview
@Composable
private fun CropCanvasLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            CropCanvas(
                imageBytes = ByteArray(0),
                corners = defaultPreviewCorners(),
                onCornersChanged = {},
            )
        }
    }
}
