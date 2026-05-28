package com.scandoc.presentation.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import com.scandoc.domain.usecase.crop.SaveDocumentUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CropViewModel(
    private val applyPerspective: ApplyPerspectiveUseCase,
    private val applyFilter: ApplyFilterUseCase,
    private val saveDocument: SaveDocumentUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CropState())
    val state: StateFlow<CropState> = _state.asStateFlow()

    private val _effects = Channel<CropEffect>(Channel.BUFFERED)
    val effects: Flow<CropEffect> = _effects.receiveAsFlow()

    fun onIntent(intent: CropIntent) {
        when (intent) {
            is CropIntent.SetImage -> _state.update { it.copy(imageBytes = intent.imageBytes) }
            is CropIntent.UpdateCorners -> _state.update { it.copy(corners = intent.corners) }
            is CropIntent.SelectFilter -> _state.update { it.copy(activeFilter = intent.filter) }
            CropIntent.Confirm -> confirm()
            CropIntent.Retake -> viewModelScope.launch { _effects.send(CropEffect.NavigateBack) }
        }
    }

    private fun confirm() {
        val current = _state.value
        val corners = current.corners
        if (corners == null) {
            viewModelScope.launch { _effects.send(CropEffect.ShowError("No document edges selected")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            try {
                val perspectiveResult = applyPerspective(current.imageBytes, corners)
                val processedResult = when (perspectiveResult) {
                    is Outcome.Success -> applyFilter(perspectiveResult.value, current.activeFilter)
                    is Outcome.Failure -> perspectiveResult
                }

                when (processedResult) {
                    is Outcome.Success -> {
                        when (val saved = saveDocument("Scan", processedResult.value)) {
                            is Outcome.Success -> _effects.send(CropEffect.NavigateToViewer(saved.value.id))
                            is Outcome.Failure -> _effects.send(
                                CropEffect.ShowError(saved.error.message ?: "Save failed"),
                            )
                        }
                    }
                    is Outcome.Failure -> _effects.send(
                        CropEffect.ShowError(processedResult.error.message ?: "Crop failed"),
                    )
                }
            } finally {
                _state.update { it.copy(isProcessing = false) }
            }
        }
    }
}
