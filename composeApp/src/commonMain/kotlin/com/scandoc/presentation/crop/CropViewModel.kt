package com.scandoc.presentation.crop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.Page
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
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
import kotlinx.datetime.Clock

class CropViewModel(
    private val applyPerspective: ApplyPerspectiveUseCase,
    private val applyFilter: ApplyFilterUseCase,
    private val imageRepository: ImageRepository,
    private val documentRepository: DocumentRepository,
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
        if (current.imageBytes.isEmpty()) return
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }

            val correctedResult = current.corners?.let { corners ->
                applyPerspective(current.imageBytes, corners)
            } ?: Outcome.Success(current.imageBytes)

            val processedBytes = when (correctedResult) {
                is Outcome.Success -> correctedResult.value
                is Outcome.Failure -> {
                    _state.update { it.copy(isProcessing = false, error = correctedResult.error.message) }
                    return@launch
                }
            }

            val filteredResult = applyFilter(processedBytes, current.activeFilter)
            val filteredBytes = when (filteredResult) {
                is Outcome.Success -> filteredResult.value
                is Outcome.Failure -> {
                    _state.update { it.copy(isProcessing = false, error = filteredResult.error.message) }
                    return@launch
                }
            }

            val documentId = "doc-${Clock.System.now().toEpochMilliseconds()}"
            val pageId = "page-${Clock.System.now().toEpochMilliseconds()}"

            when (val saveImageResult = imageRepository.saveImage(filteredBytes, documentId, 0)) {
                is Outcome.Failure -> {
                    _state.update { it.copy(isProcessing = false, error = saveImageResult.error.message) }
                    return@launch
                }
                is Outcome.Success -> {
                    val now = Clock.System.now()
                    val document = Document(
                        id = documentId,
                        name = "Scan ${now.toEpochMilliseconds()}",
                        createdAt = now,
                        updatedAt = now,
                        pages = listOf(
                            Page(
                                id = pageId,
                                orderIndex = 0,
                                imagePath = saveImageResult.value,
                                ocrResult = null,
                                width = 1080,
                                height = 1440,
                            )
                        ),
                        thumbnailPath = saveImageResult.value,
                        tags = emptyList(),
                    )
                    when (val saveDocResult = documentRepository.save(document)) {
                        is Outcome.Failure -> {
                            _state.update { it.copy(isProcessing = false, error = saveDocResult.error.message) }
                        }
                        is Outcome.Success -> {
                            _state.update { it.copy(isProcessing = false) }
                            _effects.send(CropEffect.NavigateToViewer(documentId))
                        }
                    }
                }
            }
        }
    }
}
