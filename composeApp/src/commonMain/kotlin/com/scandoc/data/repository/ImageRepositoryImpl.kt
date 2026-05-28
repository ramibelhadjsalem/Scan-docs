package com.scandoc.data.repository

import com.scandoc.core.result.Outcome
import com.scandoc.domain.repository.ImageRepository

class ImageRepositoryImpl : ImageRepository {
    // Implemented in Phase 4
    override suspend fun saveImage(bytes: ByteArray, documentId: String, pageIndex: Int): Outcome<String> =
        Outcome.Success("")
    override suspend fun loadImage(path: String): Outcome<ByteArray> =
        Outcome.Success(ByteArray(0))
    override suspend fun deleteImage(path: String): Outcome<Unit> =
        Outcome.Success(Unit)
}
