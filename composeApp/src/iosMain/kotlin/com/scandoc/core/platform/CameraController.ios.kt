package com.scandoc.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.fileDataRepresentation
import platform.AVFoundation.hasTorch
import platform.AVFoundation.torchMode
import platform.Foundation.NSError
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * iOS actual — wraps AVCaptureSession for camera control.
 *
 * Camera preview: the preview layer (AVCaptureVideoPreviewLayer) must be
 * hosted in a UIView inside a UIKitView {} composable in the camera screen.
 * This controller manages the session lifecycle and still-photo capture
 * independently of the preview.
 *
 * Frame streaming (frames Flow) is not wired to AVCaptureVideoDataOutput
 * in this implementation because Compose already handles the preview via UIKitView.
 * Use capture() for single-shot still images.
 */
@OptIn(ExperimentalForeignApi::class)
actual class PlatformCameraController actual constructor() : com.scandoc.domain.platform.CameraController {

    private val session = AVCaptureSession()
    private val sessionQueue: dispatch_queue_t =
        dispatch_queue_create("com.scandoc.camera.session", null)
    private val photoOutput = AVCapturePhotoOutput()

    private val _frameChannel = Channel<ByteArray>(Channel.CONFLATED)
    actual override val frames: Flow<ByteArray> = _frameChannel.receiveAsFlow()

    actual override suspend fun start() {
        dispatch_async(sessionQueue) {
            configureSession()
            if (!session.isRunning()) session.startRunning()
        }
    }

    actual override suspend fun stop() {
        dispatch_async(sessionQueue) {
            if (session.isRunning()) session.stopRunning()
        }
    }

    actual override suspend fun capture(): ByteArray = suspendCancellableCoroutine { cont ->
        val settings = AVCapturePhotoSettings.photoSettings()
        val delegate = PhotoDelegate(
            onCapture = { bytes -> cont.resume(bytes) },
            onError = { error -> cont.resumeWithException(RuntimeException(error.localizedDescription)) },
        )
        dispatch_async(dispatch_get_main_queue()) {
            photoOutput.capturePhotoWithSettings(settings, delegate)
        }
    }

    actual override suspend fun setFlash(enabled: Boolean) {
        val device = backCamera() ?: return
        runCatching {
            device.lockForConfiguration(null)
            if (device.hasTorch) {
                device.torchMode = if (enabled) AVTorchModeOn else AVTorchModeOff
            }
            device.unlockForConfiguration()
        }
    }

    private fun configureSession() {
        session.beginConfiguration()
        session.sessionPreset = AVCaptureSessionPresetPhoto

        val device = backCamera() ?: run {
            session.commitConfiguration()
            return
        }

        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val input = AVCaptureDeviceInput(device = device, error = errorPtr.ptr)
            val error = errorPtr.value
            if (error != null || input == null) {
                session.commitConfiguration()
                return
            }
            if (session.canAddInput(input)) session.addInput(input)
        }

        if (session.canAddOutput(photoOutput)) session.addOutput(photoOutput)

        photoOutput.isHighResolutionCaptureEnabled = true

        session.commitConfiguration()
    }

    private fun backCamera(): AVCaptureDevice? =
        AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
}

private class PhotoDelegate(
    private val onCapture: (ByteArray) -> Unit,
    private val onError: (NSError) -> Unit,
) : platform.darwin.NSObject(), AVCapturePhotoCaptureDelegate {

    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: NSError?,
    ) {
        if (error != null) {
            onError(error)
            return
        }
        val data = didFinishProcessingPhoto.fileDataRepresentation()
        if (data == null) {
            onError(NSError(domain = "ScanDoc", code = -1, userInfo = null))
            return
        }
        val bytes = data.toByteArray()
        onCapture(bytes)
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
