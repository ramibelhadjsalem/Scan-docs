package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Page
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun OcrTextView(
    page: Page?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ScanDocDimens.spaceMd),
    ) {
        Text(
            text = page?.ocrResult?.fullText?.takeIf { it.isNotBlank() } ?: "No OCR text for this page",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
