package com.scandoc.presentation.camera.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.scandoc.domain.model.DocumentCorners

@Composable
fun DetectionOverlay(
    corners: DocumentCorners?,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val detected = corners ?: return@Canvas
        val points = listOf(
            Offset(detected.topLeft.x, detected.topLeft.y),
            Offset(detected.topRight.x, detected.topRight.y),
            Offset(detected.bottomRight.x, detected.bottomRight.y),
            Offset(detected.bottomLeft.x, detected.bottomLeft.y),
        )
        points.zipWithNext().forEach { (start, end) ->
            drawLine(
                color = Color(0xFFC2F542),
                start = start,
                end = end,
                strokeWidth = 4f,
                cap = StrokeCap.Round,
            )
        }
        drawLine(
            color = Color(0xFFC2F542),
            start = points.last(),
            end = points.first(),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
        )
        points.forEach { point ->
            drawCircle(
                color = Color(0xFFC2F542),
                radius = 10f,
                center = point,
                style = Stroke(width = 4f),
            )
        }
    }
}
