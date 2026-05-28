package com.scandoc.presentation.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.core.session.ScanSessionHolder
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import com.scandoc.domain.usecase.document.CreateDocumentUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CropViewModel(
    private val applyPerspective: ApplyPerspectiveUseCase,
    private val applyFilter: ApplyFilterUseCase,
    private val createDocument: CreateDocumentUseCase,
    private val sessionHolder: ScanSessionHolder,
) : ViewModel() {

    private val _state = MutableStateFlow(CropState())
    val state: StateFlow<CropState> = _state.asStateFlow()

    private val _effects = Channel<CropEffect>(Channel.BUFFERED)
    val effects: Flow<CropEffect> = _effects.receiveAsFlow()

    fun onIntent(intent: CropIntent) {
        when (intent) {
            is CropIntent.SetImage -> _state.update {
                it.copy(
                    imageBytes = intent.imageBytes,
                    pendingPageCount = sessionHolder.pageCount,
                    corners = null,
                    error = null,
                )
            }
            is CropIntent.UpdateCorners -> _state.update { it.copy(corners = intent.corners) }
            is CropIntent.SelectFilter -> _state.update { it.copy(activeFilter = intent.filter) }
            CropIntent.Confirm -> processPage(thenFinish = true)
            CropIntent.AddAnotherPage -> processPage(thenFinish = false)
            CropIntent.Retake -> viewModelScope.launch { _effects.send(CropEffect.NavigateBack) }
        }
    }

    private fun processPage(thenFinish: Boolean) {
        val current = _state.value
        val corners = current.corners ?: run {
            viewModelScope.launch { _effects.send(CropEffect.ShowError("No document edges detected")) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }

            val perspective = applyPerspective(current.imageBytes, corners)
            val processedResult = when (perspective) {
                is Outcome.Success -> applyFilter(perspective.value, current.activeFilter)
                is Outcome.Failure -> perspective
            }

            when (processedResult) {
                is Outcome.Success -> {
                    sessionHolder.addPage(processedResult.value)
                    if (thenFinish) {
                        val docName = "Scan ${Clock.System.now().toEpochMilliseconds()}"
                        when (val result = createDocument(docName, sessionHolder.drainPages())) {
                            is Outcome.Success -> {
                                _state.update { it.copy(isProcessing = false) }
                                _effects.send(CropEffect.NavigateToViewer(result.value))
                            }
                            is Outcome.Failure -> {
                                _state.update { it.copy(isProcessing = false, error = result.error.message) }
                                _effects.send(CropEffect.ShowError(result.error.message ?: "Save failed"))
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(isProcessing = false, pendingPageCount = sessionHolder.pageCount)
                        }
                        _effects.send(CropEffect.NavigateToCamera)
                    }
                }
                is Outcome.Failure -> {
                    _state.update { it.copy(isProcessing = false, error = processedResult.error.message) }
                    _effects.send(CropEffect.ShowError(processedResult.error.message ?: "Crop failed"))
                }
            }
        }
    }
}
