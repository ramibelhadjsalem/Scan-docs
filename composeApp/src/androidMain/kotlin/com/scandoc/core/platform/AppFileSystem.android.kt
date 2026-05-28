package com.scandoc.core.platform

import android.content.Context
import okio.FileSystem
import okio.Path.Companion.toPath

actual class AppFileSystem(
    context: Context,
) : com.scandoc.domain.platform.AppFileSystem {
    actual override val documentsDir: String =
        context.filesDir.resolve("documents").absolutePath

    actual override val cacheDir: String =
        context.cacheDir.resolve("scandoc").absolutePath

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
}
