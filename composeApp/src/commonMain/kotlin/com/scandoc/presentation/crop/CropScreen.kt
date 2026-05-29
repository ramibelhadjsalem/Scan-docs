package com.scandoc.presentation.crop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocGridBackground
import com.scandoc.presentation.component.ScanDocIconButton
import com.scandoc.presentation.crop.component.CropCanvas
import com.scandoc.presentation.crop.component.FilterPicker
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CropScreen(
    state: CropState,
    onIntent: (CropIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScanDocGridBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(ScanDocDimens.spaceLg),
            verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ScanDocIconButton(text = "‹", onClick = { onIntent(CropIntent.Retake) })
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Crop",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = if (state.pendingPageCount > 0) {
                            "Page ${state.pendingPageCount + 1}"
                        } else {
                            "Page preview"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                ScanDocIconButton(text = "✓", onClick = { onIntent(CropIntent.Confirm) })
            }

            CropCanvas(
                corners = state.corners,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            FilterPicker(
                selected = state.activeFilter,
                onSelected = { onIntent(CropIntent.SelectFilter(it)) },
            )

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            ScanDocButton(
                text = when {
                    state.isProcessing -> "Processing…"
                    state.pendingPageCount > 0 -> "Save ${state.pendingPageCount + 1}-page document"
                    else -> "Save scan"
                },
                onClick = { onIntent(CropIntent.Confirm) },
                enabled = !state.isProcessing,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
            ) {
                ScanDocButton(
                    text = "Add page",
                    onClick = { onIntent(CropIntent.AddAnotherPage) },
                    enabled = !state.isProcessing,
                    compact = true,
                    modifier = Modifier.weight(1f),
                )

                ScanDocButton(
                    text = "Retake",
                    onClick = { onIntent(CropIntent.Retake) },
                    compact = true,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
