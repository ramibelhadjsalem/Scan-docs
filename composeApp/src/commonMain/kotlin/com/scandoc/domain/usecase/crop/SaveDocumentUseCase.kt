package com.scandoc.domain.usecase.crop

import com.scandoc.domain.model.Document
import com.scandoc.domain.model.Page
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
import kotlinx.datetime.Clock
import kotlin.random.Random

class SaveDocumentUseCase(
    private val documentRepository: DocumentRepository,
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(
        name: String,
        imageBytes: ByteArray,
    ): Outcome<Document> = runCatching {
        val documentId = generateId()
        val saveResult = imageRepository.saveImage(
            bytes = imageBytes,
            documentId = documentId,
            pageIndex = 0,
        )
        val imagePath = when (saveResult) {
            is Outcome.Success -> saveResult.value
            is Outcome.Failure -> throw saveResult.error
        }
        val now = Clock.System.now()
        val page = Page(
            id = generateId(),
            orderIndex = 0,
            imagePath = imagePath,
            ocrResult = null,
            width = 0,
            height = 0,
        )
        val document = Document(
            id = documentId,
            name = name,
            createdAt = now,
            updatedAt = now,
            pages = listOf(page),
            thumbnailPath = imagePath,
            tags = emptyList(),
        )
        when (val result = documentRepository.save(document)) {
            is Outcome.Success -> document
            is Outcome.Failure -> throw result.error
        }
    }.toOutcome()

    private fun generateId(): String =
        "${Clock.System.now().toEpochMilliseconds()}-${Random.nextInt(100_000, 999_999)}"
}
