package com.scandoc.presentation.crop

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter

data class CropState(
    val imageBytes: ByteArray = ByteArray(0),
    val corners: DocumentCorners? = null,
    val activeFilter: Filter = Filter.Auto,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val pendingPageCount: Int = 0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CropState) return false
        return imageBytes.contentEquals(other.imageBytes) &&
            corners == other.corners &&
            activeFilter == other.activeFilter &&
            isProcessing == other.isProcessing &&
            error == other.error &&
            pendingPageCount == other.pendingPageCount
    }

    override fun hashCode(): Int {
        var result = imageBytes.contentHashCode()
        result = 31 * result + (corners?.hashCode() ?: 0)
        result = 31 * result + activeFilter.hashCode()
        result = 31 * result + isProcessing.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + pendingPageCount
        return result
    }
}

sealed interface CropIntent {
    data class SetImage(val imageBytes: ByteArray) : CropIntent
    data class UpdateCorners(val corners: DocumentCorners) : CropIntent
    data class SelectFilter(val filter: Filter) : CropIntent
    data object Confirm : CropIntent
    data object AddAnotherPage : CropIntent
    data object Retake : CropIntent
}

sealed interface CropEffect {
    data class NavigateToViewer(val documentId: String) : CropEffect
    data object NavigateBack : CropEffect
    data object NavigateToCamera : CropEffect
    data class ShowError(val message: String) : CropEffect
}
