package com.scandoc.data.local.mapper

import com.scandoc.db.Document as DocumentEntity
import com.scandoc.db.Page as PageEntity
import com.scandoc.domain.model.Document
import com.scandoc.domain.model.OcrResult
import com.scandoc.domain.model.Page
import kotlinx.datetime.Instant

fun DocumentEntity.toDomain(pages: List<PageEntity>): Document = Document(
    id = id,
    name = name,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    pages = pages.map { it.toDomain() },
    thumbnailPath = thumbnailPath,
    tags = if (tags.isBlank()) emptyList() else tags.split(","),
)

fun PageEntity.toDomain(): Page = Page(
    id = id,
    orderIndex = orderIndex.toInt(),
    imagePath = imagePath,
    ocrResult = ocrText?.let { OcrResult(it, emptyList(), ocrConfidence?.toFloat() ?: 0f, null) },
    width = width.toInt(),
    height = height.toInt(),
)
