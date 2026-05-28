package com.scandoc.data.storage

import com.scandoc.domain.platform.AppFileSystem

class FileStorage(
    private val fileSystem: AppFileSystem,
) {
    val documentsDir: String
        get() = fileSystem.documentsDir

    suspend fun write(path: String, bytes: ByteArray): String {
        fileSystem.write(path, bytes)
        return path
    }

    suspend fun read(path: String): ByteArray =
        fileSystem.read(path)

    suspend fun delete(path: String) {
        fileSystem.delete(path)
    }
}
