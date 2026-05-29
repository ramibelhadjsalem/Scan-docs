package com.scandoc.presentation.crop

import androidx.lifecycle.viewModelScope
import com.scandoc.core.session.ScanSessionHolder
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import com.scandoc.domain.usecase.document.CreateDocumentUseCase
import com.scandoc.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CropViewModel(
    private val applyPerspective: ApplyPerspectiveUseCase,
    private val applyFilter: ApplyFilterUseCase,
    private val createDocument: CreateDocumentUseCase,
    private val sessionHolder: ScanSessionHolder,
) : BaseViewModel<CropState, CropEffect>(CropState()) {

    fun onIntent(intent: CropIntent) {
        when (intent) {
            is CropIntent.SetImage -> updateState {
                it.copy(
                    imageBytes = intent.imageBytes,
                    pendingPageCount = sessionHolder.pageCount,
                    corners = null,
                    error = null,
                )
            }
            is CropIntent.UpdateCorners -> updateState { it.copy(corners = intent.corners) }
            is CropIntent.SelectFilter -> updateState { it.copy(activeFilter = intent.filter) }
            CropIntent.Confirm -> processPage(thenFinish = true)
            CropIntent.AddAnotherPage -> processPage(thenFinish = false)
            CropIntent.Retake -> postEffect(CropEffect.NavigateBack)
        }
    }

    private fun processPage(thenFinish: Boolean) {
        val current = currentState
        val corners = current.corners ?: run {
            postEffect(CropEffect.ShowError("No document edges detected"))
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isProcessing = true, error = null) }

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
                                updateState { it.copy(isProcessing = false) }
                                sendEffect(CropEffect.NavigateToViewer(result.value))
                            }
                            is Outcome.Failure -> {
                                updateState { it.copy(isProcessing = false, error = result.error.message) }
                                sendEffect(CropEffect.ShowError(result.error.message ?: "Save failed"))
                            }
                        }
                    } else {
                        updateState {
                            it.copy(isProcessing = false, pendingPageCount = sessionHolder.pageCount)
                        }
                        sendEffect(CropEffect.NavigateToCamera)
                    }
                }
                is Outcome.Failure -> {
                    updateState { it.copy(isProcessing = false, error = processedResult.error.message) }
                    sendEffect(CropEffect.ShowError(processedResult.error.message ?: "Crop failed"))
                }
            }
        }
    }
}
