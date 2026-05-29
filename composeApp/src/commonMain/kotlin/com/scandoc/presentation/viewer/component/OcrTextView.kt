package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.scandoc.domain.model.Page
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocSection
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun OcrTextView(
    page: Page?,
    isRunningOcr: Boolean,
    onRunOcr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScanDocSection(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        val ocrText = page?.ocrResult?.fullText?.takeIf { it.isNotBlank() }
        if (ocrText != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = ((page.ocrResult?.confidence ?: 0f) * 100).toInt().toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs)) {
                    Text(
                        text = ocrText,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "Detected from selected page",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        } else if (isRunningOcr) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Running OCR…",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
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
                compact = true,
            )
        }
    }
}
