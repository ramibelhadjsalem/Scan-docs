package com.scandoc.presentation.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.scandoc.presentation.component.ScanDocGridBackground
import com.scandoc.presentation.theme.ScanDocDimens
import com.scandoc.presentation.tools.component.ToolSectionHeader
import com.scandoc.presentation.tools.component.ToolTile

@Composable
fun ToolsScreen(
    state: ToolsState,
    onIntent: (ToolsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.snackbarMessage) {
        val message = state.snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        onIntent(ToolsIntent.DismissSnackbar)
    }

    ScanDocGridBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            contentPadding = PaddingValues(
                start = ScanDocDimens.spaceXl,
                top = ScanDocDimens.space3xl,
                end = ScanDocDimens.spaceXl,
                bottom = ScanDocDimens.space5xl,
            ),
            verticalArrangement = Arrangement.spacedBy(ScanDocDimens.space2xl),
        ) {
            item(key = "tools_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Tools",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search tools",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }

            ToolsCatalog.sections.forEach { section ->
                item(key = section.title) {
                    Column(verticalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd)) {
                        ToolSectionHeader(title = section.title)
                        section.items.chunked(4).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                row.forEach { item ->
                                    ToolTile(
                                        item = item,
                                        onClick = { onIntent(ToolsIntent.ToolTapped(item)) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(ScanDocDimens.spaceMd),
        ) {
            SnackbarHost(snackbarHostState)
        }
    }
}
