package com.scandoc.presentation.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.model.ExportFormat
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import com.scandoc.domain.usecase.library.GetDocumentUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewerViewModel(
    private val getDocument: GetDocumentUseCase,
    private val exportPdf: ExportPdfUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ViewerState())
    val state: StateFlow<ViewerState> = _state.asStateFlow()

    private val _effects = Channel<ViewerEffect>(Channel.BUFFERED)
    val effects: Flow<ViewerEffect> = _effects.receiveAsFlow()

    fun onIntent(intent: ViewerIntent) {
        when (intent) {
            is ViewerIntent.Load -> load(intent.documentId)
            is ViewerIntent.GoToPage -> _state.update { it.copy(currentPageIndex = intent.index) }
            is ViewerIntent.SelectTab -> _state.update { it.copy(activeTab = intent.tab) }
            is ViewerIntent.Export -> export(intent.format)
            ViewerIntent.Share -> export(ExportFormat.Pdf)
        }
    }

    private fun load(documentId: String) {
        viewModelScope.launch {
            val document = getDocument(documentId)
            if (document == null) {
                _effects.send(ViewerEffect.ShowError("Document not found"))
            } else {
                _state.update { it.copy(document = document, currentPageIndex = 0, error = null) }
            }
        }
    }

    private fun export(format: ExportFormat) {
        val document = _state.value.document ?: return
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }
            when (format) {
                ExportFormat.Pdf -> {
                    when (val result = exportPdf(document)) {
                        is Outcome.Success -> _effects.send(ViewerEffect.ShareFile(result.value))
                        is Outcome.Failure -> _effects.send(
                            ViewerEffect.ShowError(result.error.message ?: "Export failed"),
                        )
                    }
                }
                ExportFormat.JpegImages,
                ExportFormat.PngImages,
                -> _effects.send(ViewerEffect.ShowError("Image export is not wired yet"))
            }
            _state.update { it.copy(isExporting = false) }
        }
    }
}
