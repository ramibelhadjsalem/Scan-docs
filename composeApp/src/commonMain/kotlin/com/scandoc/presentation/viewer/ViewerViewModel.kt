package com.scandoc.presentation.viewer

import androidx.lifecycle.viewModelScope
import com.scandoc.domain.model.ExportFormat
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import com.scandoc.domain.usecase.library.GetDocumentUseCase
import com.scandoc.domain.usecase.ocr.RunOcrOnPageUseCase
import com.scandoc.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class ViewerViewModel(
    private val getDocument: GetDocumentUseCase,
    private val exportPdf: ExportPdfUseCase,
    private val runOcrOnPage: RunOcrOnPageUseCase,
) : BaseViewModel<ViewerState, ViewerEffect>(ViewerState()) {

    fun onIntent(intent: ViewerIntent) {
        when (intent) {
            is ViewerIntent.Load -> load(intent.documentId)
            is ViewerIntent.GoToPage -> updateState { it.copy(currentPageIndex = intent.index) }
            is ViewerIntent.SelectTab -> updateState { it.copy(activeTab = intent.tab) }
            is ViewerIntent.Export -> export(intent.format)
            ViewerIntent.Share -> export(ExportFormat.Pdf)
            ViewerIntent.RunOcr -> runOcr()
            ViewerIntent.NavigateBack -> postEffect(ViewerEffect.NavigateBack)
        }
    }

    private fun load(documentId: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }
            val document = getDocument(documentId)
            if (document == null) {
                updateState { it.copy(isLoading = false, error = "Document not found") }
            } else {
                updateState { it.copy(document = document, currentPageIndex = 0, isLoading = false, error = null) }
            }
        }
    }

    private fun export(format: ExportFormat) {
        val document = currentState.document ?: return
        viewModelScope.launch {
            updateState { it.copy(isExporting = true) }
            when (format) {
                ExportFormat.Pdf -> when (val result = exportPdf(document)) {
                    is Outcome.Success -> sendEffect(ViewerEffect.ShareFile(result.value))
                    is Outcome.Failure -> sendEffect(
                        ViewerEffect.ShowError(result.error.message ?: "Export failed"),
                    )
                }
                ExportFormat.JpegImages,
                ExportFormat.PngImages,
                -> sendEffect(ViewerEffect.ShowError("Image export not yet supported"))
            }
            updateState { it.copy(isExporting = false) }
        }
    }

    private fun runOcr() {
        val document = currentState.document ?: return
        val pageIndex = currentState.currentPageIndex
        viewModelScope.launch {
            updateState { it.copy(isRunningOcr = true) }
            when (val result = runOcrOnPage(document.id, pageIndex)) {
                is Outcome.Success -> updateState { it.copy(document = result.value, isRunningOcr = false) }
                is Outcome.Failure -> {
                    updateState { it.copy(isRunningOcr = false) }
                    sendEffect(ViewerEffect.ShowError(result.error.message ?: "OCR failed"))
                }
            }
        }
    }
}
