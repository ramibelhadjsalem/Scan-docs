package com.scandoc.presentation.tools

data class ToolsState(
    val snackbarMessage: String? = null,
)

sealed interface ToolsIntent {
    data class ToolTapped(val tool: ToolItem) : ToolsIntent
    data object DismissSnackbar : ToolsIntent
}

sealed interface ToolsEffect {
    data class ShowSnackbar(val message: String) : ToolsEffect
}
