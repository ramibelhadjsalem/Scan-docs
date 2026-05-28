package com.scandoc.domain.usecase.library

import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

class ObserveDocumentsUseCase(
    private val repository: DocumentRepository,
) {
    operator fun invoke(): Flow<List<Document>> = repository.observeAll()
}
