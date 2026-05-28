package com.scandoc.presentation.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.model.ExportFormat
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import com.scandoc.domain.usecase.library.GetDocumentUseCase
import com.scandoc.domain.usecase.ocr.RunOcrOnPageUseCase
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
    private val runOcrOnPage: RunOcrOnPageUseCase,
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
            ViewerIntent.RunOcr -> runOcr()
            ViewerIntent.NavigateBack -> viewModelScope.launch { _effects.send(ViewerEffect.NavigateBack) }
        }
    }

    private fun load(documentId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val document = getDocument(documentId)
            if (document == null) {
                _state.update { it.copy(isLoading = false, error = "Document not found") }
            } else {
                _state.update { it.copy(document = document, currentPageIndex = 0, isLoading = false, error = null) }
            }
        }
    }

    private fun export(format: ExportFormat) {
        val document = _state.value.document ?: return
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }
            when (format) {
                ExportFormat.Pdf -> when (val result = exportPdf(document)) {
                    is Outcome.Success -> _effects.send(ViewerEffect.ShareFile(result.value))
                    is Outcome.Failure -> _effects.send(
                        ViewerEffect.ShowError(result.error.message ?: "Export failed"),
                    )
                }
                ExportFormat.JpegImages,
                ExportFormat.PngImages,
                -> _effects.send(ViewerEffect.ShowError("Image export not yet supported"))
            }
            _state.update { it.copy(isExporting = false) }
        }
    }

    private fun runOcr() {
        val document = _state.value.document ?: return
        val pageIndex = _state.value.currentPageIndex
        viewModelScope.launch {
            _state.update { it.copy(isRunningOcr = true) }
            when (val result = runOcrOnPage(document.id, pageIndex)) {
                is Outcome.Success -> _state.update { it.copy(document = result.value, isRunningOcr = false) }
                is Outcome.Failure -> {
                    _state.update { it.copy(isRunningOcr = false) }
                    _effects.send(ViewerEffect.ShowError(result.error.message ?: "OCR failed"))
                }
            }
        }
    }
}
