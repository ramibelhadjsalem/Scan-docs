package com.scandoc.presentation.crop.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.scandoc.domain.model.DocumentCorners

@Composable
fun CropCanvas(
    corners: DocumentCorners?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Document preview",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val frame = corners ?: return@Canvas
            val path = Path().apply {
                moveTo(frame.topLeft.x, frame.topLeft.y)
                lineTo(frame.topRight.x, frame.topRight.y)
                lineTo(frame.bottomRight.x, frame.bottomRight.y)
                lineTo(frame.bottomLeft.x, frame.bottomLeft.y)
                close()
            }
            drawPath(
                path = path,
                color = Color(0xFFC2F542),
                style = Stroke(width = 4f),
            )
            listOf(
                Offset(frame.topLeft.x, frame.topLeft.y),
                Offset(frame.topRight.x, frame.topRight.y),
                Offset(frame.bottomRight.x, frame.bottomRight.y),
                Offset(frame.bottomLeft.x, frame.bottomLeft.y),
            ).forEach { drawCircle(Color(0xFFC2F542), radius = 9f, center = it) }
        }
    }
}
