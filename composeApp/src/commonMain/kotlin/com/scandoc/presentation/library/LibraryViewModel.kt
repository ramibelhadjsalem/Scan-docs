package com.scandoc.presentation.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.RenameDocumentUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val observeDocuments: ObserveDocumentsUseCase,
    private val searchDocuments: SearchDocumentsUseCase,
    private val deleteDocument: DeleteDocumentUseCase,
    private val renameDocument: RenameDocumentUseCase,
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
            is LibraryIntent.ShowRenameDialog -> _state.update {
                it.copy(renamingDocument = intent.document, renameInput = intent.document.name)
            }
            is LibraryIntent.UpdateRenameInput -> _state.update { it.copy(renameInput = intent.name) }
            LibraryIntent.ConfirmRename -> confirmRename()
            LibraryIntent.DismissRenameDialog -> _state.update {
                it.copy(renamingDocument = null, renameInput = "")
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            observeDocuments()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load documents") }
                }
                .collect { docs ->
                    _state.update { it.copy(documents = docs, isLoading = false, error = null) }
                }
        }
    }

    private fun search(query: String) {
        _state.update { it.copy(query = query) }
        viewModelScope.launch {
            searchDocuments(query)
                .catch { _effects.send(LibraryEffect.ShowError(it.message ?: "Search failed")) }
                .collect { docs -> _state.update { it.copy(documents = docs) } }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch { deleteDocument(id) }
    }

    private fun confirmRename() {
        val doc = _state.value.renamingDocument ?: return
        val newName = _state.value.renameInput.trim()
        if (newName.isEmpty()) {
            viewModelScope.launch { _effects.send(LibraryEffect.ShowError("Name cannot be empty")) }
            return
        }
        _state.update { it.copy(renamingDocument = null, renameInput = "") }
        viewModelScope.launch {
            when (val result = renameDocument(doc.id, newName)) {
                is Outcome.Success -> Unit
                is Outcome.Failure -> _effects.send(
                    LibraryEffect.ShowError(result.error.message ?: "Rename failed"),
                )
            }
        }
    }
}
