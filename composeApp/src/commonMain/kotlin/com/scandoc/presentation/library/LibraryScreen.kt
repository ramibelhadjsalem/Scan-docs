package com.scandoc.presentation.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.Page
import com.scandoc.presentation.library.component.DocumentRow
import com.scandoc.presentation.library.component.EmptyState
import com.scandoc.presentation.library.component.SearchBar
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.theme.ScanDocTheme
import kotlinx.datetime.Instant
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    state: LibraryState,
    onIntent: (LibraryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = ScanDocColors.Ink,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ScanDoc",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ScanDocColors.Signal,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScanDocColors.Ink,
                    scrolledContainerColor = ScanDocColors.Ink,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(LibraryIntent.StartScan) },
                containerColor = ScanDocColors.Signal,
                contentColor = ScanDocColors.Ink,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Start scan",
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = { onIntent(LibraryIntent.Search(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScanDocDimens.spaceMd),
            )

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = ScanDocColors.Signal,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
                state.documents.isEmpty() -> {
                    EmptyState(
                        title = "No documents yet",
                        subtitle = "Tap + to scan your first document",
                        actionLabel = "Start Scanning",
                        onAction = { onIntent(LibraryIntent.StartScan) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(
                            items = state.documents,
                            key = { it.id },
                        ) { doc ->
                            DocumentRow(
                                document = doc,
                                onOpen = { onIntent(LibraryIntent.OpenDocument(doc.id)) },
                                onDelete = { onIntent(LibraryIntent.DeleteDocument(doc.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

private val previewDocuments = listOf(
    Document(
        id = "doc-001",
        name = "Q4 Financial Report",
        createdAt = Instant.parse("2024-10-01T09:00:00Z"),
        updatedAt = Instant.parse("2024-12-15T14:30:00Z"),
        pages = listOf(
            Page(id = "p1", orderIndex = 0, imagePath = "/fake/p1.jpg", ocrResult = null, width = 1080, height = 1920),
            Page(id = "p2", orderIndex = 1, imagePath = "/fake/p2.jpg", ocrResult = null, width = 1080, height = 1920),
            Page(id = "p3", orderIndex = 2, imagePath = "/fake/p3.jpg", ocrResult = null, width = 1080, height = 1920),
        ),
        thumbnailPath = null,
        tags = listOf("finance", "quarterly"),
    ),
    Document(
        id = "doc-002",
        name = "Passport Scan",
        createdAt = Instant.parse("2024-11-20T08:15:00Z"),
        updatedAt = Instant.parse("2024-11-20T08:16:00Z"),
        pages = listOf(
            Page(id = "p4", orderIndex = 0, imagePath = "/fake/p4.jpg", ocrResult = null, width = 1080, height = 1920),
        ),
        thumbnailPath = null,
        tags = listOf("identity"),
    ),
)

@Preview
@Composable
fun LibraryScreenDarkPreview() {
    ScanDocTheme {
        LibraryScreen(
            state = LibraryState(
                documents = previewDocuments,
                query = "",
                isLoading = false,
                error = null,
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
fun LibraryScreenLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface {
            LibraryScreen(
                state = LibraryState(
                    documents = previewDocuments,
                    query = "Report",
                    isLoading = false,
                    error = null,
                ),
                onIntent = {},
            )
        }
    }
}
