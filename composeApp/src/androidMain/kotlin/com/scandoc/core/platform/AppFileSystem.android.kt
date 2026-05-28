package com.scandoc.core.platform

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AppFileSystem : FileSystemAccess, KoinComponent {
    private val context: Context by inject()

    actual val documentsDir: String get() = context.filesDir.absolutePath
    actual val cacheDir: String get() = context.cacheDir.absolutePath

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
