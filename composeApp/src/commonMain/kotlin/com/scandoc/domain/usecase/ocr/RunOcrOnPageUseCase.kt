package com.scandoc.domain.usecase.ocr

import com.scandoc.domain.model.Document
import com.scandoc.domain.platform.OcrEngine
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome

class RunOcrOnPageUseCase(
    private val documentRepository: DocumentRepository,
    private val imageRepository: ImageRepository,
    private val ocrEngine: OcrEngine,
) {
    suspend operator fun invoke(documentId: String, pageIndex: Int): Outcome<Document> {
        val document = documentRepository.getById(documentId)
            ?: return Outcome.Failure(NoSuchElementException("Document $documentId not found"))
        val page = document.pages.getOrNull(pageIndex)
            ?: return Outcome.Failure(IndexOutOfBoundsException("No page at index $pageIndex"))

        val imageBytes = when (val result = imageRepository.loadImage(page.imagePath)) {
            is Outcome.Success -> result.value
            is Outcome.Failure -> return result
        }

        val ocrResult = runCatching { ocrEngine.recognize(imageBytes) }.toOutcome()
        val recognized = when (ocrResult) {
            is Outcome.Success -> ocrResult.value
            is Outcome.Failure -> return ocrResult
        }

        val updatedPages = document.pages.mapIndexed { i, p ->
            if (i == pageIndex) p.copy(ocrResult = recognized) else p
        }
        val updatedDocument = document.copy(pages = updatedPages)

        return when (val saveResult = documentRepository.save(updatedDocument)) {
            is Outcome.Success -> Outcome.Success(updatedDocument)
            is Outcome.Failure -> saveResult
        }
    }
}
