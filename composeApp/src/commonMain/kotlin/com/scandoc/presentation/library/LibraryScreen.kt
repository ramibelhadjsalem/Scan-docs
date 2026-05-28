package com.scandoc.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.scandoc.presentation.component.ScanDocButton
import com.scandoc.presentation.library.component.DocumentRow
import com.scandoc.presentation.library.component.EmptyState
import com.scandoc.presentation.library.component.SearchBar
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun LibraryScreen(
    state: LibraryState,
    onIntent: (LibraryIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(ScanDocDimens.spaceLg),
        verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceLg),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceXs)) {
            Text(
                text = "ScanDoc",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Private document scanning",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SearchBar(
            query = state.query,
            onQueryChange = { onIntent(LibraryIntent.Search(it)) },
        )

        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.documents.isEmpty() -> EmptyState(
                    onStartScan = { onIntent(LibraryIntent.StartScan) },
                    modifier = Modifier.align(Alignment.Center),
                )
                else -> LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceSm),
                    contentPadding = PaddingValues(bottom = ScanDocDimens.space5xl),
                ) {
                    items(state.documents, key = { it.id }) { document ->
                        DocumentRow(
                            document = document,
                            onOpen = { onIntent(LibraryIntent.OpenDocument(document.id)) },
                        )
                    }
                }
            }
        }

        ScanDocButton(
            text = "New scan",
            onClick = { onIntent(LibraryIntent.StartScan) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
