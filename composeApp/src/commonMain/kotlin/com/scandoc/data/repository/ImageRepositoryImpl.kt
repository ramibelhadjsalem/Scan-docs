package com.scandoc.data.repository

import com.scandoc.domain.platform.AppFileSystem
import com.scandoc.domain.repository.ImageRepository
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome

class ImageRepositoryImpl(
    private val fileSystem: AppFileSystem,
) : ImageRepository {

    override suspend fun saveImage(bytes: ByteArray, documentId: String, pageIndex: Int): Outcome<String> =
        runCatching {
            val path = imagePath(documentId = documentId, pageIndex = pageIndex)
            fileSystem.write(path, bytes)
            path
        }.toOutcome()

    override suspend fun loadImage(path: String): Outcome<ByteArray> =
        runCatching { fileSystem.read(path) }.toOutcome()

    override suspend fun deleteImage(path: String): Outcome<Unit> =
        runCatching { fileSystem.delete(path) }.toOutcome()

    private fun imagePath(documentId: String, pageIndex: Int): String {
        val directory = fileSystem.documentsDir.trimEnd('/')
        val relativePath = "$documentId/page_$pageIndex.jpg"
        return if (directory.isEmpty()) relativePath else "$directory/$relativePath"
    }
}
