package com.scandoc.data.local.mapper

import com.scandoc.domain.model.Document
import com.scandoc.domain.model.OcrResult
import com.scandoc.domain.model.Page
import kotlinx.datetime.Instant
import com.scandoc.db.Document as DocumentRow
import com.scandoc.db.Page as PageRow

fun DocumentRow.toDomain(pages: List<PageRow>): Document =
    Document(
        id = id,
        name = name,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
        pages = pages.map { it.toDomain() },
        thumbnailPath = thumbnailPath,
        tags = tags.toDomainTags(),
    )

fun PageRow.toDomain(): Page =
    Page(
        id = id,
        orderIndex = orderIndex.toInt(),
        imagePath = imagePath,
        ocrResult = toOcrResult(),
        width = width.toInt(),
        height = height.toInt(),
    )

fun List<String>.toStorageTags(): String =
    joinToString(separator = ",")

fun String.toDomainTags(): List<String> =
    split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }

private fun PageRow.toOcrResult(): OcrResult? {
    val text = ocrText ?: return null
    val confidence = ocrConfidence ?: return null
    return OcrResult(
        fullText = text,
        blocks = emptyList(),
        confidence = confidence.toFloat(),
        language = null,
    )
}
