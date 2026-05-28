package com.scandoc.core.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * iOS actual for [CameraController].
 * Full AVFoundation wiring requires a Swift/Objective-C bridge and is deferred to a future
 * iteration. This stub compiles and satisfies the interface contract; [frames] never emits,
 * [capture] returns an empty byte array, and all lifecycle calls are no-ops.
 */
actual class PlatformCameraController actual constructor() : CameraController {

    private val _frames = MutableSharedFlow<ByteArray>(extraBufferCapacity = 1)
    override val frames: Flow<ByteArray> = _frames.asSharedFlow()

    override suspend fun start() = Unit
    override suspend fun stop() = Unit
    override suspend fun capture(): ByteArray = ByteArray(0)
    override suspend fun setFlash(enabled: Boolean) = Unit
}
