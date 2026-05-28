package com.scandoc.presentation.viewer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OcrTextView(
    ocrResult: OcrResult?,
    modifier: Modifier = Modifier,
) {
    if (ocrResult == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Text(
                text = "No text recognized",
                style = MaterialTheme.typography.bodyMedium,
                color = ScanDocColors.Text3,
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ScanDocDimens.spaceMd),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Text Recognition",
                style = MaterialTheme.typography.labelLarge,
                color = ScanDocColors.Text,
            )
            Text(
                text = "${(ocrResult.confidence * 100).toInt()}% confidence",
                style = MaterialTheme.typography.labelSmall,
                color = ScanDocColors.Signal,
            )
        }

        HorizontalDivider(color = ScanDocColors.Line)

        SelectionContainer {
            Text(
                text = ocrResult.fullText,
                style = MaterialTheme.typography.bodyMedium,
                color = ScanDocColors.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(ScanDocDimens.spaceMd),
            )
        }
    }
}

private val previewOcrResult = OcrResult(
    fullText = "Invoice #INV-2024-0042\n\nDate: December 15, 2024\nDue: January 15, 2025\n\n" +
        "Bill To:\nAcme Corporation\n123 Business Ave\nSan Francisco, CA 94105\n\n" +
        "Description              Amount\n" +
        "Consulting Services      \$4,500.00\n" +
        "Software License         \$1,200.00\n\n" +
        "Total Due:               \$5,700.00",
    blocks = emptyList<OcrBlock>(),
    confidence = 0.94f,
    language = "en",
)

@Preview
@Composable
fun OcrTextViewDarkPreview() {
    ScanDocTheme {
        Surface(
            color = ScanDocColors.Ink,
            modifier = Modifier.fillMaxSize(),
        ) {
            OcrTextView(
                ocrResult = previewOcrResult,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun OcrTextViewLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OcrTextView(
                ocrResult = null,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
