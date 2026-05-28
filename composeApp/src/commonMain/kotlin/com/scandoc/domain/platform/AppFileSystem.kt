package com.scandoc.domain.platform

interface AppFileSystem {
    val documentsDir: String
    val cacheDir: String
    suspend fun write(path: String, bytes: ByteArray)
    suspend fun read(path: String): ByteArray
    suspend fun delete(path: String)
    fun exists(path: String): Boolean
}
