package com.scandoc.presentation.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.component.ScanDocChip
import com.scandoc.presentation.component.ScanDocGridBackground
import com.scandoc.presentation.component.ScanDocIconButton
import com.scandoc.presentation.library.hasOcrText
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
    when {
        state.isLoading -> Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        state.document == null -> ScanDocGridBackground(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(ScanDocDimens.spaceLg),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
                ) {
                    Text(
                        text = state.error ?: "Document not found",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    ScanDocButton(
                        text = "Go back",
                        onClick = { onIntent(ViewerIntent.NavigateBack) },
                        compact = true,
                    )
                }
            }
        }

        else -> {
            val document = state.document
            val page = document.pages.getOrNull(state.currentPageIndex)

            ScanDocGridBackground(modifier = modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(ScanDocDimens.spaceLg),
                    verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ScanDocIconButton(text = "‹", onClick = { onIntent(ViewerIntent.NavigateBack) })
                        Column(
                            modifier = Modifier.weight(1f).padding(horizontal = ScanDocDimens.spaceMd),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = document.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                            )
                            Text(
                                text = "${document.pages.size} pages · ${if (document.hasOcrText()) "OCR complete" else "OCR pending"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                            )
                        }
                        ScanDocIconButton(text = "⋯", onClick = { onIntent(ViewerIntent.Share) })
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs)) {
                        ViewerTab.entries.forEach { tab ->
                            ScanDocChip(
                                text = tab.label,
                                selected = state.activeTab == tab,
                                onClick = { onIntent(ViewerIntent.SelectTab(tab)) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    when (state.activeTab) {
                        ViewerTab.Image -> PageCarousel(
                            pages = document.pages,
                            currentPageIndex = state.currentPageIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        )
                        ViewerTab.Text -> {
                            PageCarousel(
                                pages = document.pages,
                                currentPageIndex = state.currentPageIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                            )
                            OcrTextView(
                                page = page,
                                isRunningOcr = state.isRunningOcr,
                                onRunOcr = { onIntent(ViewerIntent.RunOcr) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        ViewerTab.Pdf -> {
                            PageCarousel(
                                pages = document.pages,
                                currentPageIndex = state.currentPageIndex,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                            )
                            ExportSheet(
                                onExport = { onIntent(ViewerIntent.Export(it)) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    if (state.isExporting) {
                        Text(
                            text = "Exporting…",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    state.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

private val ViewerTab.label: String
    get() = when (this) {
        ViewerTab.Image -> "Pages"
        ViewerTab.Text -> "Text"
        ViewerTab.Pdf -> "Export"
    }
