package com.scandoc.domain.model

data class OcrResult(
    val fullText: String,
    val blocks: List<OcrBlock>,
    val confidence: Float,
    val language: String?,
)
