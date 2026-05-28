package com.scandoc.presentation.library

import com.scandoc.domain.model.Document

data class LibraryState(
    val documents: List<Document> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = true,
    val error: String? = null,
    val renamingDocument: Document? = null,
    val renameInput: String = "",
)

sealed interface LibraryIntent {
    data object Load : LibraryIntent
    data class Search(val query: String) : LibraryIntent
    data class OpenDocument(val id: String) : LibraryIntent
    data class DeleteDocument(val id: String) : LibraryIntent
    data object StartScan : LibraryIntent
    data class ShowRenameDialog(val document: Document) : LibraryIntent
    data class UpdateRenameInput(val name: String) : LibraryIntent
    data object ConfirmRename : LibraryIntent
    data object DismissRenameDialog : LibraryIntent
}

sealed interface LibraryEffect {
    data class NavigateToViewer(val id: String) : LibraryEffect
    data object NavigateToCamera : LibraryEffect
    data class ShowError(val message: String) : LibraryEffect
}
