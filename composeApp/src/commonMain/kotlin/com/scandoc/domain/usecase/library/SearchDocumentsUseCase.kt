package com.scandoc.domain.usecase.library

import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

class SearchDocumentsUseCase(
    private val repository: DocumentRepository,
) {
    operator fun invoke(query: String): Flow<List<Document>> = repository.search(query)
}
