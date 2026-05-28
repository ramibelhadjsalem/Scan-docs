package com.scandoc.domain.usecase.library

import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.result.Outcome
import kotlinx.datetime.Clock

class RenameDocumentUseCase(
    private val repository: DocumentRepository,
) {
    suspend operator fun invoke(id: String, name: String): Outcome<Unit> {
        val document = repository.getById(id)
            ?: return Outcome.Failure(NoSuchElementException("Document $id not found"))
        return repository.save(document.copy(name = name.trim(), updatedAt = Clock.System.now()))
    }
}
