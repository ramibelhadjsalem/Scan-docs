package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.ExportFormat
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocSection
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ExportSheet(
    onExport: (ExportFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScanDocSection(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
        ) {
            ScanDocButton(
                text = "PDF",
                onClick = { onExport(ExportFormat.Pdf) },
                compact = true,
                modifier = Modifier.weight(1f),
            )
            ScanDocButton(
                text = "JPG",
                onClick = { onExport(ExportFormat.JpegImages) },
                compact = true,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
