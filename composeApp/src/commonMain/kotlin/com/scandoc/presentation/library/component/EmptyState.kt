package com.scandoc.presentation.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocShapes
import com.scandoc.presentation.theme.ScanDocTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(ScanDocDimens.space2xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(ScanDocShapes.Large),
        ) {
            Surface(
                color = ScanDocColors.SignalGlow,
                shape = ScanDocShapes.Large,
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = ScanDocColors.Signal,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = ScanDocColors.Text,
            textAlign = TextAlign.Center,
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = ScanDocColors.Text3,
            textAlign = TextAlign.Center,
        )

        if (actionLabel != null && onAction != null) {
            ScanDocButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.padding(top = ScanDocDimens.spaceSm),
            )
        }
    }
}

@Preview
@Composable
fun EmptyStateDarkPreview() {
    ScanDocTheme {
        Surface(
            color = ScanDocColors.Ink,
            modifier = Modifier.fillMaxSize(),
        ) {
            EmptyState(
                title = "No documents yet",
                subtitle = "Tap + to scan your first document",
                actionLabel = "Start Scanning",
                onAction = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Preview
@Composable
fun EmptyStateLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            EmptyState(
                title = "No documents yet",
                subtitle = "Tap + to scan your first document",
                actionLabel = "Start Scanning",
                onAction = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
