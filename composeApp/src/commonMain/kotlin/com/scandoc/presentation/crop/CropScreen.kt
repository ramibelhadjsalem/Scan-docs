package com.scandoc.presentation.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.crop.component.CropCanvas
import com.scandoc.presentation.crop.component.FilterPicker
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun CropScreen(
    state: CropState,
    onIntent: (CropIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(ScanDocDimens.spaceLg),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Adjust scan",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (state.pendingPageCount > 0) {
                Text(
                    text = "${state.pendingPageCount} page(s) captured",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        CropCanvas(
            corners = state.corners,
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
        )

        FilterPicker(
            selected = state.activeFilter,
            onSelected = { onIntent(CropIntent.SelectFilter(it)) },
        )

        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
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

        ScanDocButton(
            text = "Add another page",
            onClick = { onIntent(CropIntent.AddAnotherPage) },
            enabled = !state.isProcessing,
            modifier = Modifier.fillMaxWidth(),
        )

        ScanDocButton(
            text = "Retake",
            onClick = { onIntent(CropIntent.Retake) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
