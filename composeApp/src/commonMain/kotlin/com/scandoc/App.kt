package com.scandoc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.scandoc.presentation.library.LibraryScreen
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.theme.ScanDocTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    ScanDocTheme {
        val viewModel = koinViewModel<LibraryViewModel>()
        val state by viewModel.state.collectAsState()
        LibraryScreen(
            state = state,
            onIntent = viewModel::onIntent,
        )
    }
}
