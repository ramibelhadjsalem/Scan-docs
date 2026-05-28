package com.scandoc.data.repository

import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.data.storage.FileStorage
import com.scandoc.domain.repository.ImageRepository

class ImageRepositoryImpl(
    private val storage: FileStorage,
) : ImageRepository {

    override suspend fun saveImage(
        bytes: ByteArray,
        documentId: String,
        pageIndex: Int,
    ): Outcome<String> = runCatching {
        val path = storage.imagePathFor(documentId, pageIndex)
        storage.write(path, bytes)
        path
    }.toOutcome()

    override suspend fun loadImage(path: String): Outcome<ByteArray> =
        runCatching { storage.read(path) }.toOutcome()

    override suspend fun deleteImage(path: String): Outcome<Unit> =
        runCatching { storage.delete(path) }.toOutcome()
}
