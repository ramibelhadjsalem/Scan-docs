package com.scandoc.domain.usecase.document

import com.scandoc.domain.model.Document
import com.scandoc.domain.model.Page
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
import com.scandoc.domain.result.Outcome
import kotlinx.datetime.Clock

class CreateDocumentUseCase(
    private val documentRepository: DocumentRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(name: String, pageBytes: List<ByteArray>): Outcome<String> {
        val documentId = "doc-${Clock.System.now().toEpochMilliseconds()}"
        val pages = mutableListOf<Page>()

        pageBytes.forEachIndexed { index, bytes ->
            when (val result = imageRepository.saveImage(bytes, documentId, index)) {
                is Outcome.Success -> pages.add(
                    Page(
                        id = "page-$documentId-$index",
                        orderIndex = index,
                        imagePath = result.value,
                        ocrResult = null,
                        width = 0,
                        height = 0,
                    )
                )
                is Outcome.Failure -> return result
            }
        }

        val now = Clock.System.now()
        val document = Document(
            id = documentId,
            name = name.ifBlank { "Scan $documentId" },
            createdAt = now,
            updatedAt = now,
            pages = pages,
            thumbnailPath = pages.firstOrNull()?.imagePath,
            tags = emptyList(),
        )

        return when (val result = documentRepository.save(document)) {
            is Outcome.Success -> Outcome.Success(documentId)
            is Outcome.Failure -> result
        }
    }
}
