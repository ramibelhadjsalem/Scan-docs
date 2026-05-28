package com.scandoc.presentation.camera.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ShutterButton(
    onClick: () -> Unit,
    countdown: Int?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.scale(scale),
    ) {
        Surface(
            onClick = onClick,
            enabled = enabled,
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(4.dp, Color.White),
            modifier = Modifier.size(80.dp),
            interactionSource = interactionSource,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp),
            ) {
                if (countdown != null) {
                    Text(
                        text = countdown.toString(),
                        color = ScanDocColors.Signal,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                } else {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(60.dp),
                    ) {}
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShutterButtonDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp),
            ) {
                ShutterButton(
                    onClick = {},
                    countdown = null,
                    enabled = true,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShutterButtonLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp),
            ) {
                ShutterButton(
                    onClick = {},
                    countdown = 3,
                    enabled = true,
                )
            }
        }
    }
}
