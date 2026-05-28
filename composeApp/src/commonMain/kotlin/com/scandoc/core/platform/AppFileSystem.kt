package com.scandoc.core.platform

/**
 * Platform-specific file system access.
 * Android: Context.filesDir + Okio.
 * iOS: NSFileManager documents directory + Okio.
 */
expect class AppFileSystem {
    val documentsDir: String
    val cacheDir: String
    suspend fun write(path: String, bytes: ByteArray)
    suspend fun read(path: String): ByteArray
    suspend fun delete(path: String)
    fun exists(path: String): Boolean
}
