package com.scandoc.domain.usecase.library

import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository

class GetDocumentUseCase(
    private val repository: DocumentRepository,
) {
    suspend operator fun invoke(id: String): Document? =
        repository.getById(id)
}
