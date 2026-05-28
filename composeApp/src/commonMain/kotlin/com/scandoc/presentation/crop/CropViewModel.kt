package com.scandoc.presentation.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
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
            val perspective = applyPerspective(current.imageBytes, corners)
            val processed = when (perspective) {
                is Outcome.Success -> applyFilter(perspective.value, current.activeFilter)
                is Outcome.Failure -> perspective
            }

            when (processed) {
                is Outcome.Success -> _effects.send(CropEffect.NavigateToViewer("draft-scan"))
                is Outcome.Failure -> _effects.send(
                    CropEffect.ShowError(processed.error.message ?: "Crop failed"),
                )
            }
            _state.update { it.copy(isProcessing = false) }
        }
    }
}
