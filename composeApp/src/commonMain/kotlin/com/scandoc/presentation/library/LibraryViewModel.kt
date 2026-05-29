package com.scandoc.presentation.library

import androidx.lifecycle.viewModelScope
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.RenameDocumentUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import com.scandoc.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val observeDocuments: ObserveDocumentsUseCase,
    private val searchDocuments: SearchDocumentsUseCase,
    private val deleteDocument: DeleteDocumentUseCase,
    private val renameDocument: RenameDocumentUseCase,
) : BaseViewModel<LibraryState, LibraryEffect>(LibraryState()) {

    init {
        onIntent(LibraryIntent.Load)
    }

    fun onIntent(intent: LibraryIntent) {
        when (intent) {
            LibraryIntent.Load -> load()
            is LibraryIntent.Search -> search(intent.query)
            is LibraryIntent.OpenDocument -> viewModelScope.launch {
                sendEffect(LibraryEffect.NavigateToViewer(intent.id))
            }
            is LibraryIntent.DeleteDocument -> delete(intent.id)
            LibraryIntent.StartScan -> viewModelScope.launch {
                sendEffect(LibraryEffect.NavigateToCamera)
            }
            is LibraryIntent.ShowRenameDialog -> updateState {
                it.copy(renamingDocument = intent.document, renameInput = intent.document.name)
            }
            is LibraryIntent.UpdateRenameInput -> updateState { it.copy(renameInput = intent.name) }
            LibraryIntent.ConfirmRename -> confirmRename()
            LibraryIntent.DismissRenameDialog -> updateState {
                it.copy(renamingDocument = null, renameInput = "")
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }
            observeDocuments()
                .catch { e ->
                    updateState { it.copy(isLoading = false, error = e.message ?: "Failed to load documents") }
                }
                .collect { docs ->
                    val recent = docs.sortedByDescending { it.updatedAt }.take(3)
                    updateState {
                        it.copy(
                            documents = docs,
                            recentDocuments = recent,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }

    private fun search(query: String) {
        updateState { it.copy(query = query) }
        viewModelScope.launch {
            searchDocuments(query)
                .catch { sendEffect(LibraryEffect.ShowError(it.message ?: "Search failed")) }
                .collect { docs -> updateState { it.copy(documents = docs) } }
        }
    }

    private fun delete(id: String) {
        viewModelScope.launch { deleteDocument(id) }
    }

    private fun confirmRename() {
        val doc = currentState.renamingDocument ?: return
        val newName = currentState.renameInput.trim()
        if (newName.isEmpty()) {
            postEffect(LibraryEffect.ShowError("Name cannot be empty"))
            return
        }
        updateState { it.copy(renamingDocument = null, renameInput = "") }
        viewModelScope.launch {
            when (val result = renameDocument(doc.id, newName)) {
                is Outcome.Success -> Unit
                is Outcome.Failure -> sendEffect(
                    LibraryEffect.ShowError(result.error.message ?: "Rename failed"),
                )
            }
        }
    }
}
