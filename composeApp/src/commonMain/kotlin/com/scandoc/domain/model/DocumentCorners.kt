package com.scandoc.domain.model

data class DocumentCorners(
    val topLeft: Offset,
    val topRight: Offset,
    val bottomRight: Offset,
    val bottomLeft: Offset,
)

data class Offset(
    val x: Float,
    val y: Float,
)
