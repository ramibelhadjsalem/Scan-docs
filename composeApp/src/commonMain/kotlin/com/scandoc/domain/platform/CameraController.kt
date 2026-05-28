package com.scandoc.domain.platform

import kotlinx.coroutines.flow.Flow

interface CameraController {
    val frames: Flow<ByteArray>
    suspend fun start()
    suspend fun stop()
    suspend fun capture(): ByteArray
    suspend fun setFlash(enabled: Boolean)
}
