package com.scandoc.fake

import com.scandoc.domain.result.Outcome
import com.scandoc.domain.repository.ImageRepository

class FakeImageRepository : ImageRepository {

    val savedImages = mutableMapOf<String, ByteArray>()
    var shouldFail = false

    override suspend fun saveImage(bytes: ByteArray, documentId: String, pageIndex: Int): Outcome<String> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        val path = "$documentId/page_$pageIndex.jpg"
        savedImages[path] = bytes
        return Outcome.Success(path)
    }

    override suspend fun loadImage(path: String): Outcome<ByteArray> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        val bytes = savedImages[path] ?: return Outcome.Failure(RuntimeException("not found: $path"))
        return Outcome.Success(bytes)
    }

    override suspend fun deleteImage(path: String): Outcome<Unit> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        savedImages.remove(path)
        return Outcome.Success(Unit)
    }
}
