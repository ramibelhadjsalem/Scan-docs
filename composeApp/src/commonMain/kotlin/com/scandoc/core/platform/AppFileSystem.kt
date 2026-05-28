package com.scandoc.core.platform

/**
 * Platform-specific file system access.
 * Android: Context.filesDir + Okio.
 * iOS: NSFileManager documents directory + Okio.
 */
expect class AppFileSystem : com.scandoc.domain.platform.AppFileSystem {
    override val documentsDir: String
    override val cacheDir: String
    override suspend fun write(path: String, bytes: ByteArray)
    override suspend fun read(path: String): ByteArray
    override suspend fun delete(path: String)
    override fun exists(path: String): Boolean
}
