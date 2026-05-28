package com.scandoc.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val observeDocuments: ObserveDocumentsUseCase,
    private val searchDocuments: SearchDocumentsUseCase,
    private val deleteDocument: DeleteDocumentUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    private val _effects = Channel<LibraryEffect>(Channel.BUFFERED)
    val effects: Flow<LibraryEffect> = _effects.receiveAsFlow()

    init {
        onIntent(LibraryIntent.Load)
    }

    fun onIntent(intent: LibraryIntent) {
        when (intent) {
            LibraryIntent.Load -> load()
            is LibraryIntent.Search -> search(intent.query)
            is LibraryIntent.OpenDocument -> viewModelScope.launch {
                _effects.send(LibraryEffect.NavigateToViewer(intent.id))
            }
            is LibraryIntent.DeleteDocument -> delete(intent.id)
            LibraryIntent.StartScan -> viewModelScope.launch {
                _effects.send(LibraryEffect.NavigateToCamera)
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            observeDocuments()
                .catch { _effects.send(LibraryEffect.ShowError(it.message ?: "Unknown error")) }
                .collect { docs ->
                    _state.update { it.copy(documents = docs, isLoading = false) }
                }
        }
    }

    private fun search(query: String) {
        _state.update { it.copy(query = query) }
        viewModelScope.launch {
            searchDocuments(query)
                .catch { _effects.send(LibraryEffect.ShowError(it.message ?: "Unknown error")) }
                .collect { docs ->
                    _state.update { it.copy(documents = docs) }
                }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteDocument(id)
        }
    }
}
