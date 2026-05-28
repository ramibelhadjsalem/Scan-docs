package com.scandoc.core.platform

// Android actual — Context.filesDir + Okio (Phase 6)
actual class AppFileSystem {
    actual val documentsDir: String = ""
    actual val cacheDir: String = ""
    actual suspend fun write(path: String, bytes: ByteArray) {}
    actual suspend fun read(path: String): ByteArray = ByteArray(0)
    actual suspend fun delete(path: String) {}
    actual fun exists(path: String): Boolean = false
}
