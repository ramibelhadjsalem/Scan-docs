package com.scandoc.presentation.camera.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset as DrawOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Offset
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun Offset.toDrawOffset(width: Float, height: Float) = DrawOffset(x * width, y * height)

@Composable
fun DetectionOverlay(
    corners: DocumentCorners?,
    modifier: Modifier = Modifier,
) {
    if (corners == null) {
        Box(modifier = modifier.fillMaxSize())
        return
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val tl = corners.topLeft.toDrawOffset(size.width, size.height)
        val tr = corners.topRight.toDrawOffset(size.width, size.height)
        val br = corners.bottomRight.toDrawOffset(size.width, size.height)
        val bl = corners.bottomLeft.toDrawOffset(size.width, size.height)

        val path = Path().apply {
            moveTo(tl.x, tl.y)
            lineTo(tr.x, tr.y)
            lineTo(br.x, br.y)
            lineTo(bl.x, bl.y)
            close()
        }

        drawPath(
            path = path,
            color = ScanDocColors.Signal.copy(alpha = 0.30f),
            style = Fill,
        )
        drawPath(
            path = path,
            color = ScanDocColors.Signal,
            style = Stroke(width = 2.dp.toPx()),
        )

        val cornerRadius = 8.dp.toPx()
        listOf(tl, tr, br, bl).forEach { offset ->
            drawCircle(
                color = ScanDocColors.Signal,
                radius = cornerRadius,
                center = offset,
            )
        }
    }
}

@Preview
@Composable
private fun DetectionOverlayDarkPreview() {
    ScanDocTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScanDocColors.Ink3),
        ) {
            DetectionOverlay(
                corners = DocumentCorners(
                    topLeft = Offset(0.1f, 0.05f),
                    topRight = Offset(0.9f, 0.08f),
                    bottomRight = Offset(0.88f, 0.95f),
                    bottomLeft = Offset(0.12f, 0.92f),
                ),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
private fun DetectionOverlayLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFDDDDDD)),
            ) {
                DetectionOverlay(
                    corners = DocumentCorners(
                        topLeft = Offset(0.1f, 0.05f),
                        topRight = Offset(0.9f, 0.08f),
                        bottomRight = Offset(0.88f, 0.95f),
                        bottomLeft = Offset(0.12f, 0.92f),
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
