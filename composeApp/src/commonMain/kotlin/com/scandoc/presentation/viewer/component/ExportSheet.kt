package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.scandoc.domain.model.ExportFormat
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportSheet(
    isVisible: Boolean,
    isExporting: Boolean,
    onExport: (ExportFormat) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ScanDocColors.Ink2,
        contentColor = ScanDocColors.Text,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = ScanDocDimens.space2xl),
        ) {
            Text(
                text = "Export As",
                style = MaterialTheme.typography.titleLarge,
                color = ScanDocColors.Text,
                modifier = Modifier.padding(
                    horizontal = ScanDocDimens.spaceMd,
                    vertical = ScanDocDimens.spaceSm,
                ),
            )

            HorizontalDivider(color = ScanDocColors.Line)

            ExportOption(
                label = "PDF Document",
                sublabel = "Best for sharing",
                icon = Icons.Default.Share,
                onClick = { onExport(ExportFormat.Pdf) },
            )

            ExportOption(
                label = "JPEG Images",
                sublabel = "One image per page",
                icon = Icons.Default.Photo,
                onClick = { onExport(ExportFormat.JpegImages) },
            )

            ExportOption(
                label = "PNG Images",
                sublabel = "Lossless quality",
                icon = Icons.Default.Check,
                onClick = { onExport(ExportFormat.PngImages) },
            )

            if (isExporting) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScanDocDimens.spaceMd),
                    color = ScanDocColors.Signal,
                    trackColor = ScanDocColors.Line,
                )
            }
        }
    }
}

@Composable
private fun ExportOption(
    label: String,
    sublabel: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ScanDocColors.Text,
                )
            },
            supportingContent = {
                Text(
                    text = sublabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ScanDocColors.Text3,
                )
            },
            leadingContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ScanDocColors.Text2,
                    modifier = Modifier.size(ScanDocDimens.iconSize),
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
    }
}

@Preview
@Composable
fun ExportSheetDarkPreview() {
    ScanDocTheme {
        Surface(color = ScanDocColors.Ink2) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = ScanDocDimens.space2xl)) {
                Text(
                    text = "Export As",
                    style = MaterialTheme.typography.titleLarge,
                    color = ScanDocColors.Text,
                    modifier = Modifier.padding(
                        horizontal = ScanDocDimens.spaceMd,
                        vertical = ScanDocDimens.spaceSm,
                    ),
                )
                HorizontalDivider(color = ScanDocColors.Line)
                ExportOption(
                    label = "PDF Document",
                    sublabel = "Best for sharing",
                    icon = Icons.Default.Share,
                    onClick = {},
                )
                ExportOption(
                    label = "JPEG Images",
                    sublabel = "One image per page",
                    icon = Icons.Default.Photo,
                    onClick = {},
                )
                ExportOption(
                    label = "PNG Images",
                    sublabel = "Lossless quality",
                    icon = Icons.Default.Check,
                    onClick = {},
                )
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ScanDocDimens.spaceMd),
                    color = ScanDocColors.Signal,
                    trackColor = ScanDocColors.Line,
                )
            }
        }
    }
}

@Preview
@Composable
fun ExportSheetLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = ScanDocDimens.space2xl)) {
                Text(
                    text = "Export As",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        horizontal = ScanDocDimens.spaceMd,
                        vertical = ScanDocDimens.spaceSm,
                    ),
                )
                HorizontalDivider()
                ExportOption(
                    label = "PDF Document",
                    sublabel = "Best for sharing",
                    icon = Icons.Default.Share,
                    onClick = {},
                )
                ExportOption(
                    label = "JPEG Images",
                    sublabel = "One image per page",
                    icon = Icons.Default.Photo,
                    onClick = {},
                )
                ExportOption(
                    label = "PNG Images",
                    sublabel = "Lossless quality",
                    icon = Icons.Default.Check,
                    onClick = {},
                )
            }
        }
    }
}
