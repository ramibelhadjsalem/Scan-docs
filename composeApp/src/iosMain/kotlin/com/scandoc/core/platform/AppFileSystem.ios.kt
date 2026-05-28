package com.scandoc.core.platform

import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class AppFileSystem : com.scandoc.domain.platform.AppFileSystem {
    actual override val documentsDir: String =
        directoryInUserDomain(NSDocumentDirectory).trimEnd('/') + "/ScanDoc/documents"

    actual override val cacheDir: String =
        directoryInUserDomain(NSCachesDirectory).trimEnd('/') + "/ScanDoc"

    actual override suspend fun write(path: String, bytes: ByteArray) {
        val okioPath = path.toPath()
        okioPath.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
        FileSystem.SYSTEM.write(okioPath) {
            write(bytes)
        }
    }

    actual override suspend fun read(path: String): ByteArray =
        FileSystem.SYSTEM.read(path.toPath()) {
            readByteArray()
        }

    actual override suspend fun delete(path: String) {
        FileSystem.SYSTEM.delete(path.toPath(), mustExist = false)
    }

    actual override fun exists(path: String): Boolean =
        FileSystem.SYSTEM.exists(path.toPath())

    private fun directoryInUserDomain(directory: ULong): String {
        val paths = NSFileManager.defaultManager.URLsForDirectory(
            directory = directory,
            inDomains = NSUserDomainMask,
        )
        val url = paths.firstOrNull() as? NSURL
        return url?.path ?: error("Unable to resolve iOS directory: $directory")
    }
}
