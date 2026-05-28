package com.scandoc.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query
                .flatMapLatest { query ->
                    if (query.isBlank()) observeDocuments()
                    else searchDocuments(query)
                }
                .catch { _effects.send(LibraryEffect.ShowError(it.message ?: "Unknown error")) }
                .collect { docs ->
                    _state.update { it.copy(documents = docs, isLoading = false) }
                }
        }
    }

    fun onIntent(intent: LibraryIntent) {
        when (intent) {
            LibraryIntent.Load -> Unit
            is LibraryIntent.Search -> {
                _state.update { it.copy(query = intent.query) }
                _query.value = intent.query
            }
            is LibraryIntent.OpenDocument -> viewModelScope.launch {
                _effects.send(LibraryEffect.NavigateToViewer(intent.id))
            }
            is LibraryIntent.DeleteDocument -> delete(intent.id)
            LibraryIntent.StartScan -> viewModelScope.launch {
                _effects.send(LibraryEffect.NavigateToCamera)
            }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch {
            deleteDocument(id)
        }
    }
}
