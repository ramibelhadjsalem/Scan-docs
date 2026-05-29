package com.scandoc.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.AVFoundation.AVCaptureTorchModeOn
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.fileDataRepresentation
import platform.AVFoundation.hasTorch
import platform.AVFoundation.torchMode
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * iOS actual — wraps a shared [AVCaptureSession] for camera control.
 *
 * The session is shared with [CameraPreview] via [IosCameraSession] so
 * preview and capture are coordinated on a single session.
 */
@OptIn(ExperimentalForeignApi::class)
actual class PlatformCameraController actual constructor() : com.scandoc.domain.platform.CameraController {

    private val photoOutput = AVCapturePhotoOutput()
    private val photoDelegates = mutableSetOf<PhotoDelegate>()

    private val _frameChannel = Channel<ByteArray>(Channel.CONFLATED)
    actual override val frames: Flow<ByteArray> = _frameChannel.receiveAsFlow()

    actual override suspend fun start() {
        IosCameraSession.start(photoOutput)
    }

    actual override suspend fun stop() {
        IosCameraSession.stop()
    }

    actual override suspend fun capture(): ByteArray = suspendCancellableCoroutine { cont ->
        val settings = AVCapturePhotoSettings.photoSettings()
        val delegate = PhotoDelegate(
            onCapture = { bytes ->
                photoDelegates.remove(this)
                if (cont.isActive) {
                    cont.resume(bytes)
                }
            },
            onError = { error, photoDelegate ->
                photoDelegates.remove(photoDelegate)
                if (cont.isActive) {
                    cont.resumeWithException(IllegalStateException(error.localizedDescription))
                }
            },
        )
        cont.invokeOnCancellation {
            IosCameraSession.onSessionQueue {
                photoDelegates.remove(delegate)
            }
        }
        IosCameraSession.onSessionQueue {
            photoDelegates.add(delegate)
            IosCameraSession.configure(photoOutput)
            photoOutput.capturePhotoWithSettings(settings, delegate)
        }
    }

    actual override suspend fun setFlash(enabled: Boolean) {
        IosCameraSession.onSessionQueue {
            val device = backCamera() ?: return@onSessionQueue
            memScoped {
                val errorPtr = alloc<ObjCObjectVar<NSError?>>()
                if (device.lockForConfiguration(errorPtr.ptr)) {
                    if (device.hasTorch) {
                        device.torchMode = if (enabled) AVCaptureTorchModeOn else AVCaptureTorchModeOff
                    }
                    device.unlockForConfiguration()
                }
            }
        }
    }

    private fun backCamera(): AVCaptureDevice? =
        AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
}

private class PhotoDelegate(
    private val onCapture: PhotoDelegate.(ByteArray) -> Unit,
    private val onError: (NSError, PhotoDelegate) -> Unit,
) : NSObject(), AVCapturePhotoCaptureDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: NSError?,
    ) {
        if (error != null) {
            onError(error, this)
            return
        }
        val data = didFinishProcessingPhoto.fileDataRepresentation()
            ?: run {
                onError(NSError(domain = "ScanDoc", code = -1, userInfo = null), this)
                return
            }
        onCapture(data.toByteArray())
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun platform.Foundation.NSData.toByteArray(): ByteArray {
    val pointer = bytes ?: return ByteArray(0)
    return ByteArray(length.toInt()).also { array ->
        array.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), pointer, length)
        }
    }
}
