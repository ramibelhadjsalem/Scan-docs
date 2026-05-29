package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.PaperStackMark
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun EmptyState(
    onStartScan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ScanDocDimens.space5xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        PaperStackMark(
            modifier = Modifier
                .width(220.dp)
                .height(260.dp),
        )
        Text(
            text = "No scans yet",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Capture your first document to build a searchable library.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        ScanDocButton(text = "Start scan", onClick = onStartScan, compact = true)
    }
}
