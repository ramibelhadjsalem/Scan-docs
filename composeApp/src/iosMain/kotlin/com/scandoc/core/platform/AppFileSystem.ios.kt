package com.scandoc.core.platform

// iOS actual — NSFileManager (Phase 6)
actual class AppFileSystem : com.scandoc.domain.platform.AppFileSystem {
    actual override val documentsDir: String = ""
    actual override val cacheDir: String = ""
    actual override suspend fun write(path: String, bytes: ByteArray) {}
    actual override suspend fun read(path: String): ByteArray = ByteArray(0)
    actual override suspend fun delete(path: String) {}
    actual override fun exists(path: String): Boolean = false
}
