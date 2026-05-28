package com.scandoc.domain.usecase.library

import com.scandoc.domain.result.Outcome
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository

class DeleteDocumentUseCase(
    private val documentRepository: DocumentRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(id: String): Outcome<Unit> =
        documentRepository.delete(id)
}
