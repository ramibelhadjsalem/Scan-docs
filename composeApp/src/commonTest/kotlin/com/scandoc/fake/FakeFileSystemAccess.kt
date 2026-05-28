package com.scandoc.fake

import com.scandoc.core.platform.FileSystemAccess

class FakeFileSystemAccess(
    override val documentsDir: String = "/fake/documents",
    override val cacheDir: String = "/fake/cache",
) : FileSystemAccess {
    val written = mutableMapOf<String, ByteArray>()
    var shouldThrow = false

    override suspend fun write(path: String, bytes: ByteArray) {
        if (shouldThrow) throw IllegalStateException("Write failure")
        written[path] = bytes
    }

    override suspend fun read(path: String): ByteArray =
        written[path] ?: error("No file at $path")

    override suspend fun delete(path: String) {
        written.remove(path)
    }

    override fun exists(path: String): Boolean = written.containsKey(path)
}
