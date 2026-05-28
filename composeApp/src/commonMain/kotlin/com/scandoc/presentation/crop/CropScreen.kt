package com.scandoc.presentation.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceLg),
    ) {
        Text(
            text = "Adjust scan",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
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
            text = if (state.isProcessing) "Processing" else "Save scan",
            onClick = { onIntent(CropIntent.Confirm) },
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
