package com.scandoc.domain.repository

import com.scandoc.domain.result.Outcome

interface ImageRepository {
    suspend fun saveImage(bytes: ByteArray, documentId: String, pageIndex: Int): Outcome<String>
    suspend fun loadImage(path: String): Outcome<ByteArray>
    suspend fun deleteImage(path: String): Outcome<Unit>
}
