package com.scandoc.data.storage

import com.scandoc.core.platform.AppFileSystem

class FileStorage(private val appFileSystem: AppFileSystem) {

    val documentsDir: String get() = appFileSystem.documentsDir

    fun imagePathFor(documentId: String, pageIndex: Int, ext: String = "jpg"): String =
        "${appFileSystem.documentsDir}/images/$documentId/page_$pageIndex.$ext"

    suspend fun write(path: String, bytes: ByteArray) = appFileSystem.write(path, bytes)
    suspend fun read(path: String): ByteArray = appFileSystem.read(path)
    suspend fun delete(path: String) = appFileSystem.delete(path)
    fun exists(path: String): Boolean = appFileSystem.exists(path)
}
