package com.scandoc.data.repository

import com.scandoc.data.storage.FileStorage
import com.scandoc.domain.repository.ImageRepository
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome

class ImageRepositoryImpl(
    private val storage: FileStorage,
) : ImageRepository {

    override suspend fun saveImage(bytes: ByteArray, documentId: String, pageIndex: Int): Outcome<String> =
        runCatching {
            storage.write(
                path = imagePath(documentId = documentId, pageIndex = pageIndex),
                bytes = bytes,
            )
        }.toOutcome()

    override suspend fun loadImage(path: String): Outcome<ByteArray> =
        runCatching { storage.read(path) }.toOutcome()

    override suspend fun deleteImage(path: String): Outcome<Unit> =
        runCatching { storage.delete(path) }.toOutcome()

    private fun imagePath(documentId: String, pageIndex: Int): String {
        val directory = storage.documentsDir.trimEnd('/')
        val relativePath = "$documentId/page_$pageIndex.jpg"
        return if (directory.isEmpty()) relativePath else "$directory/$relativePath"
    }
}
