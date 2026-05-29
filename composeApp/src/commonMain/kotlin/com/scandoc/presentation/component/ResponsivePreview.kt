package com.scandoc.presentation.component

import kotlin.math.min

const val scanDocTouchTargetDp = 48f
const val scanDocIconVisualDp = 40f

data class PreviewSize(
    val width: Float,
    val height: Float,
)

fun fittedPreviewSize(
    maxWidth: Float,
    maxHeight: Float,
    preferredWidth: Float,
    preferredHeight: Float,
): PreviewSize {
    if (maxWidth <= 0f || maxHeight <= 0f || preferredWidth <= 0f || preferredHeight <= 0f) {
        return PreviewSize(width = 0f, height = 0f)
    }

    val scale = min(1f, min(maxWidth / preferredWidth, maxHeight / preferredHeight))
    return PreviewSize(
        width = preferredWidth * scale,
        height = preferredHeight * scale,
    )
}
