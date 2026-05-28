package com.scandoc.presentation.crop.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun DraggableCorner(
    offset: androidx.compose.ui.geometry.Offset,
    onDrag: (delta: androidx.compose.ui.geometry.Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val handleSizePx = 12.dp

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (offset.x - handleSizePx.roundToPx()).roundToInt(),
                    y = (offset.y - handleSizePx.roundToPx()).roundToInt(),
                )
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = ScanDocColors.Signal,
                    shape = CircleShape,
                )
                .background(
                    color = ScanDocColors.SignalGlow,
                    shape = CircleShape,
                )
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        onDrag(dragAmount)
                    }
                },
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun DraggableCornerDarkPreview() {
    ScanDocTheme {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(ScanDocColors.Ink3),
        ) {
            DraggableCorner(
                offset = androidx.compose.ui.geometry.Offset(40f, 40f),
                onDrag = {},
            )
            DraggableCorner(
                offset = androidx.compose.ui.geometry.Offset(160f, 40f),
                onDrag = {},
            )
            DraggableCorner(
                offset = androidx.compose.ui.geometry.Offset(160f, 160f),
                onDrag = {},
            )
            DraggableCorner(
                offset = androidx.compose.ui.geometry.Offset(40f, 160f),
                onDrag = {},
            )
        }
    }
}

@Preview
@Composable
private fun DraggableCornerLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color(0xFFE0E0E0)),
            ) {
                DraggableCorner(
                    offset = androidx.compose.ui.geometry.Offset(40f, 40f),
                    onDrag = {},
                )
                DraggableCorner(
                    offset = androidx.compose.ui.geometry.Offset(160f, 40f),
                    onDrag = {},
                )
                DraggableCorner(
                    offset = androidx.compose.ui.geometry.Offset(160f, 160f),
                    onDrag = {},
                )
                DraggableCorner(
                    offset = androidx.compose.ui.geometry.Offset(40f, 160f),
                    onDrag = {},
                )
            }
        }
    }
}
