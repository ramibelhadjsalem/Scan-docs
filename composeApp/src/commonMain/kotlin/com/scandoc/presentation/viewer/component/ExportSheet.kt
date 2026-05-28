package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.ExportFormat
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun ExportSheet(
    onExport: (ExportFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs),
    ) {
        ScanDocButton(text = "PDF", onClick = { onExport(ExportFormat.Pdf) })
        ScanDocButton(text = "JPG", onClick = { onExport(ExportFormat.JpegImages) })
    }
}
