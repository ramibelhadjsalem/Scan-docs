package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Page
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun OcrTextView(
    page: Page?,
    isRunningOcr: Boolean,
    onRunOcr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ScanDocDimens.spaceMd)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        val ocrText = page?.ocrResult?.fullText?.takeIf { it.isNotBlank() }
        if (ocrText != null) {
            Text(
                text = ocrText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        } else if (isRunningOcr) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(
                text = "Running OCR…",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        } else {
            Text(
                text = "No text recognized for this page yet.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            ScanDocButton(
                text = "Run OCR",
                onClick = onRunOcr,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
