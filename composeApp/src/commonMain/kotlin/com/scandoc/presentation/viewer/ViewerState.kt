package com.scandoc.presentation.viewer

import com.scandoc.domain.model.Document
import com.scandoc.domain.model.ExportFormat

data class ViewerState(
    val document: Document? = null,
    val currentPageIndex: Int = 0,
    val activeTab: ViewerTab = ViewerTab.Image,
    val isExporting: Boolean = false,
    val error: String? = null,
)

enum class ViewerTab { Image, Text, Pdf }

sealed interface ViewerIntent {
    data class Load(val documentId: String) : ViewerIntent
    data class GoToPage(val index: Int) : ViewerIntent
    data class SelectTab(val tab: ViewerTab) : ViewerIntent
    data class Export(val format: ExportFormat) : ViewerIntent
    data object Share : ViewerIntent
}

sealed interface ViewerEffect {
    data class ShareFile(val path: String) : ViewerEffect
    data class ShowError(val message: String) : ViewerEffect
    data object NavigateBack : ViewerEffect
}
