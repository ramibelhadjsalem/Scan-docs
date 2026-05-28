package com.scandoc.core.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class AppFileSystem {

    actual val documentsDir: String = run {
        NSFileManager.defaultManager.URLsForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask,
        ).firstOrNull()
            ?.path
            ?: error("Cannot resolve iOS documents directory")
    }

    actual val cacheDir: String = run {
        NSFileManager.defaultManager.URLsForDirectory(
            NSCachesDirectory,
            NSUserDomainMask,
        ).firstOrNull()
            ?.path
            ?: error("Cannot resolve iOS cache directory")
    }

    actual suspend fun write(path: String, bytes: ByteArray) = withContext(Dispatchers.IO) {
        val okioPath = path.toPath()
        okioPath.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
        FileSystem.SYSTEM.write(okioPath) { write(bytes) }
    }

    actual suspend fun read(path: String): ByteArray = withContext(Dispatchers.IO) {
        FileSystem.SYSTEM.read(path.toPath()) { readByteArray() }
    }

    actual suspend fun delete(path: String) = withContext(Dispatchers.IO) {
        FileSystem.SYSTEM.delete(path.toPath(), mustExist = false)
    }

    actual fun exists(path: String): Boolean = FileSystem.SYSTEM.exists(path.toPath())
}
