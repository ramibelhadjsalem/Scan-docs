package com.scandoc.presentation.camera.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ModeSwitcher(
    isFlashOn: Boolean,
    isAutoCapture: Boolean,
    onToggleFlash: () -> Unit,
    onToggleAutoCapture: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXl),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        IconButton(onClick = onToggleFlash) {
            Icon(
                imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = if (isFlashOn) "Flash on" else "Flash off",
                tint = if (isFlashOn) ScanDocColors.Signal else ScanDocColors.Text2,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Auto",
                color = ScanDocColors.Text2,
                style = MaterialTheme.typography.labelLarge,
            )
            Switch(
                checked = isAutoCapture,
                onCheckedChange = onToggleAutoCapture,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ScanDocColors.Ink,
                    checkedTrackColor = ScanDocColors.Signal,
                    uncheckedThumbColor = ScanDocColors.Text2,
                    uncheckedTrackColor = ScanDocColors.Line,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun ModeSwitcherDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            ModeSwitcher(
                isFlashOn = true,
                isAutoCapture = true,
                onToggleFlash = {},
                onToggleAutoCapture = {},
                modifier = Modifier.padding(ScanDocDimens.spaceMd),
            )
        }
    }
}

@Preview
@Composable
private fun ModeSwitcherLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            ModeSwitcher(
                isFlashOn = false,
                isAutoCapture = false,
                onToggleFlash = {},
                onToggleAutoCapture = {},
                modifier = Modifier.padding(ScanDocDimens.spaceMd),
            )
        }
    }
}
