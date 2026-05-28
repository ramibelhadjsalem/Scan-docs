package com.scandoc.presentation.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.core.result.Outcome
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewerViewModel(
    private val exportPdf: ExportPdfUseCase,
    private val documentRepository: DocumentRepository,
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
            ViewerIntent.Share -> share()
        }
    }

    private fun load(documentId: String) {
        viewModelScope.launch {
            val document = documentRepository.getById(documentId)
            if (document != null) {
                _state.update { it.copy(document = document) }
            } else {
                _effects.send(ViewerEffect.ShowError("Document not found"))
            }
        }
    }

    private fun export(format: com.scandoc.domain.model.ExportFormat) {
        val document = _state.value.document ?: return
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }
            when (val result = exportPdf(document)) {
                is Outcome.Success -> _effects.send(ViewerEffect.ShareFile(result.value))
                is Outcome.Failure -> _effects.send(
                    ViewerEffect.ShowError(result.error.message ?: "Export failed"),
                )
            }
            _state.update { it.copy(isExporting = false) }
        }
    }

    private fun share() {
        val document = _state.value.document ?: return
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true) }
            when (val result = exportPdf(document)) {
                is Outcome.Success -> _effects.send(ViewerEffect.ShareFile(result.value))
                is Outcome.Failure -> _effects.send(
                    ViewerEffect.ShowError(result.error.message ?: "Share failed"),
                )
            }
            _state.update { it.copy(isExporting = false) }
        }
    }
}
