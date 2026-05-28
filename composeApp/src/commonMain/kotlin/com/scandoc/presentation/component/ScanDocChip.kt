package com.scandoc.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScanDocChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (selected) ScanDocColors.Signal else ScanDocColors.Ink3
    val textColor = if (selected) ScanDocColors.Ink else ScanDocColors.Text2
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal

    Surface(
        color = backgroundColor,
        shape = ScanDocShapes.Pill,
        modifier = modifier
            .height(36.dp)
            .clip(ScanDocShapes.Pill)
            .clickable(role = Role.Switch, onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(
                horizontal = ScanDocDimens.spaceMd,
                vertical = ScanDocDimens.spaceXxs,
            ),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = fontWeight),
                color = textColor,
            )
        }
    }
}

@Preview
@Composable
fun ScanDocChipDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
                modifier = Modifier.padding(ScanDocDimens.spaceMd),
            ) {
                ScanDocChip(label = "Auto", selected = true, onClick = {})
                ScanDocChip(label = "B&W", selected = false, onClick = {})
                ScanDocChip(label = "Photo", selected = false, onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun ScanDocChipLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
                modifier = Modifier.padding(ScanDocDimens.spaceMd),
            ) {
                ScanDocChip(label = "Auto", selected = true, onClick = {})
                ScanDocChip(label = "B&W", selected = false, onClick = {})
                ScanDocChip(label = "Photo", selected = false, onClick = {})
            }
        }
    }
}
