package com.scandoc.presentation.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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
        // Implemented in Phase 7
    }
}
