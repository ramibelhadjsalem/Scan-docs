package com.scandoc.presentation.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter
import com.scandoc.domain.model.Offset
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocButtonVariant
import com.scandoc.presentation.crop.component.CropCanvas
import com.scandoc.presentation.crop.component.FilterPicker
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private fun defaultCorners() = DocumentCorners(
    topLeft = Offset(0f, 0f),
    topRight = Offset(1f, 0f),
    bottomRight = Offset(1f, 1f),
    bottomLeft = Offset(0f, 1f),
)

@Composable
fun CropScreen(
    state: CropState,
    onIntent: (CropIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScanDocColors.Ink),
    ) {
        CropCanvas(
            imageBytes = state.imageBytes,
            corners = state.corners ?: defaultCorners(),
            onCornersChanged = { onIntent(CropIntent.UpdateCorners(it)) },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        FilterPicker(
            filters = Filter.entries,
            activeFilter = state.activeFilter,
            onFilterSelected = { onIntent(CropIntent.SelectFilter(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = ScanDocDimens.spaceSm),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ScanDocDimens.spaceMd),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScanDocButton(
                text = "Retake",
                onClick = { onIntent(CropIntent.Retake) },
                variant = ScanDocButtonVariant.Secondary,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(ScanDocDimens.spaceMd))
            ScanDocButton(
                text = "Confirm",
                onClick = { onIntent(CropIntent.Confirm) },
                isLoading = state.isProcessing,
                modifier = Modifier.weight(1f),
            )
        }

        if (state.error != null) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScanDocDimens.spaceMd)
                    .padding(bottom = ScanDocDimens.spaceMd),
                containerColor = ScanDocColors.Ink3,
                contentColor = ScanDocColors.Text,
            ) {
                Text(
                    text = state.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CropScreenPreview() {
    ScanDocTheme {
        CropScreen(
            state = CropState(
                imageBytes = ByteArray(0),
                corners = DocumentCorners(
                    topLeft = Offset(0.1f, 0.05f),
                    topRight = Offset(0.9f, 0.08f),
                    bottomRight = Offset(0.88f, 0.95f),
                    bottomLeft = Offset(0.12f, 0.92f),
                ),
                activeFilter = Filter.Auto,
                isProcessing = false,
                error = null,
            ),
            onIntent = {},
        )
    }
}
