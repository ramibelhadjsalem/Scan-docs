package com.scandoc.presentation.tools

import com.scandoc.presentation.base.BaseViewModel

class ToolsViewModel : BaseViewModel<ToolsState, ToolsEffect>(ToolsState()) {
    fun onIntent(intent: ToolsIntent) {
        when (intent) {
            is ToolsIntent.ToolTapped -> {
                val message = "${intent.tool.label} - coming soon"
                updateState { it.copy(snackbarMessage = message) }
                postEffect(ToolsEffect.ShowSnackbar(message))
            }

            ToolsIntent.DismissSnackbar -> updateState { it.copy(snackbarMessage = null) }
        }
    }
}
