package com.scandoc.domain.model

data class Page(
    val id: String,
    val orderIndex: Int,
    val imagePath: String,
    val ocrResult: OcrResult?,
    val width: Int,
    val height: Int,
)
