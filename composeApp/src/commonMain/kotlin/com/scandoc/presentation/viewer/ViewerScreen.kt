package com.scandoc.presentation.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.ExportFormat
import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import com.scandoc.domain.model.Page
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocTheme
import com.scandoc.presentation.viewer.component.ExportSheet
import com.scandoc.presentation.viewer.component.OcrTextView
import com.scandoc.presentation.viewer.component.PageCarousel
import kotlinx.datetime.Instant
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    state: ViewerState,
    onIntent: (ViewerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showExportSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = ScanDocColors.Ink,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.document?.name ?: "Document",
                        style = MaterialTheme.typography.titleLarge,
                        color = ScanDocColors.Text,
                        maxLines = 1,
                    )
                },
                actions = {
                    IconButton(onClick = { onIntent(ViewerIntent.Share) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = ScanDocColors.Text2,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScanDocColors.Ink,
                    scrolledContainerColor = ScanDocColors.Ink,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showExportSheet = true },
                containerColor = ScanDocColors.Signal,
                contentColor = ScanDocColors.Ink,
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Export",
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            TabRow(
                selectedTabIndex = state.activeTab.ordinal,
                containerColor = ScanDocColors.Ink2,
                contentColor = ScanDocColors.Signal,
            ) {
                ViewerTab.entries.forEach { tab ->
                    Tab(
                        selected = tab == state.activeTab,
                        onClick = { onIntent(ViewerIntent.SelectTab(tab)) },
                        text = {
                            Text(
                                text = tab.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (tab == state.activeTab) ScanDocColors.Signal else ScanDocColors.Text3,
                            )
                        },
                    )
                }
            }

            when (state.activeTab) {
                ViewerTab.Image -> {
                    PageCarousel(
                        pages = state.document?.pages ?: emptyList(),
                        currentPageIndex = state.currentPageIndex,
                        onPageChanged = { onIntent(ViewerIntent.GoToPage(it)) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                }
                ViewerTab.Text -> {
                    OcrTextView(
                        ocrResult = state.document?.pages?.getOrNull(state.currentPageIndex)?.ocrResult,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    )
                }
                ViewerTab.Pdf -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "PDF Preview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ScanDocColors.Text3,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }

        ExportSheet(
            isVisible = showExportSheet,
            isExporting = state.isExporting,
            onExport = { format: ExportFormat ->
                onIntent(ViewerIntent.Export(format))
                showExportSheet = false
            },
            onDismiss = { showExportSheet = false },
        )
    }
}

private val previewDocument = Document(
    id = "doc-001",
    name = "Q4 Financial Report",
    createdAt = Instant.parse("2024-10-01T09:00:00Z"),
    updatedAt = Instant.parse("2024-12-15T14:30:00Z"),
    pages = listOf(
        Page(
            id = "p1",
            orderIndex = 0,
            imagePath = "/fake/p1.jpg",
            ocrResult = OcrResult(
                fullText = "Invoice #INV-2024-0042\n\nDate: December 15, 2024\n\nTotal Due: \$5,700.00",
                blocks = emptyList<OcrBlock>(),
                confidence = 0.94f,
                language = "en",
            ),
            width = 1080,
            height = 1920,
        ),
        Page(
            id = "p2",
            orderIndex = 1,
            imagePath = "/fake/p2.jpg",
            ocrResult = null,
            width = 1080,
            height = 1920,
        ),
    ),
    thumbnailPath = null,
    tags = listOf("finance", "quarterly"),
)

@Preview
@Composable
fun ViewerScreenDarkPreview() {
    ScanDocTheme {
        ViewerScreen(
            state = ViewerState(
                document = previewDocument,
                currentPageIndex = 0,
                activeTab = ViewerTab.Image,
                isExporting = false,
                error = null,
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
fun ViewerScreenLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            ViewerScreen(
                state = ViewerState(
                    document = previewDocument,
                    currentPageIndex = 0,
                    activeTab = ViewerTab.Text,
                    isExporting = false,
                    error = null,
                ),
                onIntent = {},
            )
        }
    }
}
