package com.scandoc.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ScanDocGridBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ScanDocColors.Ink3,
                        ScanDocColors.Ink,
                    ),
                ),
            ),
    ) {
        SquareGrid(modifier = Modifier.matchParentSize())
        content()
    }
}

@Composable
fun SquareGrid(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val step = 32.dp.toPx()
        val gridColor = ScanDocColors.Text.copy(alpha = 0.035f)
        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f,
            )
            x += step
        }
        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
            )
            y += step
        }
    }
}

@Composable
fun ScanDocIconButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(scanDocTouchTargetDp.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(scanDocIconVisualDp.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = ScanDocColors.Text.copy(alpha = 0.12f),
                    shape = CircleShape,
                )
                .background(ScanDocColors.Text.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ScanDocPill(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val background = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        ScanDocColors.Text.copy(alpha = 0.06f)
    }
    val foreground = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(100.dp))
            .border(
                width = 1.dp,
                color = ScanDocColors.Text.copy(alpha = if (selected) 0f else 0.1f),
                shape = RoundedCornerShape(100.dp),
            )
            .background(background)
            .padding(horizontal = ScanDocDimens.spaceMd),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = foreground,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@Composable
fun PaperPreview(
    modifier: Modifier = Modifier,
    label: String? = null,
    accent: Color = MaterialTheme.colorScheme.primary,
    showCorners: Boolean = false,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ScanDocColors.Paper),
    ) {
        PaperLines(modifier = Modifier.matchParentSize())
        label?.let {
            Text(
                text = it,
                modifier = Modifier.align(Alignment.Center),
                color = ScanDocColors.Ink.copy(alpha = 0.62f),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
        if (showCorners) {
            DetectionCorners(
                modifier = Modifier
                    .matchParentSize()
                    .padding(ScanDocDimens.spaceMd),
                color = accent,
            )
        }
    }
}

@Composable
fun PaperStackMark(
    modifier: Modifier = Modifier,
    animatedScan: Boolean = false,
) {
    var scanProgress by remember { mutableFloatStateOf(0.48f) }
    LaunchedEffect(animatedScan) {
        if (!animatedScan) {
            scanProgress = 0.48f
            return@LaunchedEffect
        }
        var startMillis: Long? = null
        while (true) {
            withFrameMillis { frameMillis ->
                val start = startMillis ?: frameMillis.also { startMillis = it }
                val phase = ((frameMillis - start) % 2_800L).toFloat() / 2_800f
                scanProgress = if (phase < 0.5f) {
                    0.22f + phase * 1.04f
                } else {
                    0.74f - (phase - 0.5f) * 1.04f
                }
            }
        }
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(180.dp)
                .height(238.dp)
                .graphicsLayer {
                    rotationZ = 7f
                    translationX = 18.dp.toPx()
                    translationY = 14.dp.toPx()
                }
                .clip(RoundedCornerShape(8.dp))
                .background(ScanDocColors.Deep.copy(alpha = 0.34f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(174.dp)
                .height(232.dp)
                .graphicsLayer {
                    rotationZ = 3f
                    translationX = 10.dp.toPx()
                    translationY = 7.dp.toPx()
                }
                .clip(RoundedCornerShape(8.dp))
                .background(ScanDocColors.Warm.copy(alpha = 0.34f)),
        )
        PaperPreview(
            modifier = Modifier
                .align(Alignment.Center)
                .width(168.dp)
                .height(226.dp)
                .graphicsLayer { rotationZ = -5f },
            showCorners = true,
        )
        Canvas(modifier = Modifier.matchParentSize()) {
            val y = size.height * scanProgress
            drawLine(
                color = ScanDocColors.Signal.copy(alpha = 0.86f),
                start = Offset(size.width * 0.18f, y),
                end = Offset(size.width * 0.82f, y),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawLine(
                color = ScanDocColors.Signal.copy(alpha = 0.24f),
                start = Offset(size.width * 0.2f, y + 4.dp.toPx()),
                end = Offset(size.width * 0.8f, y + 4.dp.toPx()),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
fun DetectionCorners(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Canvas(modifier = modifier) {
        val length = 32.dp.toPx()
        val stroke = 3.dp.toPx()
        val path = Path().apply {
            moveTo(0f, length)
            lineTo(0f, 0f)
            lineTo(length, 0f)

            moveTo(size.width - length, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, length)

            moveTo(size.width, size.height - length)
            lineTo(size.width, size.height)
            lineTo(size.width - length, size.height)

            moveTo(length, size.height)
            lineTo(0f, size.height)
            lineTo(0f, size.height - length)
        }
        drawPath(path = path, color = color, style = Stroke(width = stroke))
    }
}

@Composable
private fun PaperLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val lineColor = ScanDocColors.Ink.copy(alpha = 0.12f)
        var y = 18.dp.toPx()
        while (y <= size.height - 12.dp.toPx()) {
            drawLine(
                color = lineColor,
                start = Offset(10.dp.toPx(), y),
                end = Offset(size.width - 10.dp.toPx(), y),
                strokeWidth = 1.dp.toPx(),
            )
            y += 18.dp.toPx()
        }

        val columnColor = ScanDocColors.Ink.copy(alpha = 0.035f)
        var x = 16.dp.toPx()
        while (x <= size.width) {
            drawLine(
                color = columnColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1.dp.toPx(),
            )
            x += 22.dp.toPx()
        }
    }
}

@Composable
fun ScanDocSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = ScanDocColors.Text.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
            )
            .background(ScanDocColors.Text.copy(alpha = 0.045f))
            .padding(ScanDocDimens.spaceMd),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
    ) {
        content()
    }
}

@Composable
fun PaperThumbnail(
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(54.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ScanDocColors.Paper),
        contentAlignment = Alignment.Center,
    ) {
        PaperLines(modifier = Modifier.matchParentSize())
        Text(
            text = pageCount.toString(),
            color = ScanDocColors.Ink.copy(alpha = 0.66f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
