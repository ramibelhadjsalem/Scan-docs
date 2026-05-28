package com.scandoc.domain.model

import kotlinx.datetime.Instant

data class Document(
    val id: String,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val pages: List<Page>,
    val thumbnailPath: String?,
    val tags: List<String>,
)
