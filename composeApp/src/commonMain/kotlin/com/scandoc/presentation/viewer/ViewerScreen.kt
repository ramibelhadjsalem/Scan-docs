package com.scandoc.presentation.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.ScanDocChip
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.viewer.component.ExportSheet
import com.scandoc.presentation.viewer.component.OcrTextView
import com.scandoc.presentation.viewer.component.PageCarousel

@Composable
fun ViewerScreen(
    state: ViewerState,
    onIntent: (ViewerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val document = state.document
    val page = document?.pages?.getOrNull(state.currentPageIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(ScanDocDimens.spaceLg),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        Text(
            text = document?.name ?: "Document",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs)) {
            ViewerTab.entries.forEach { tab ->
                ScanDocChip(
                    text = tab.name,
                    selected = state.activeTab == tab,
                    onClick = { onIntent(ViewerIntent.SelectTab(tab)) },
                )
            }
        }

        when (state.activeTab) {
            ViewerTab.Image -> PageCarousel(
                pages = document?.pages.orEmpty(),
                currentPageIndex = state.currentPageIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
            )
            ViewerTab.Text -> OcrTextView(
                page = page,
                modifier = Modifier.weight(1f),
            )
            ViewerTab.Pdf -> ExportSheet(
                onExport = { onIntent(ViewerIntent.Export(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (state.isExporting) {
            Text(
                text = "Exporting",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
