package com.scandoc.core.platform

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVMediaTypeVideo
import platform.Foundation.NSError
import platform.darwin.dispatch_async
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t

/**
 * Shared iOS camera session singleton.
 *
 * Both [CameraPreview] and [PlatformCameraController] use this single
 * [AVCaptureSession] instance to avoid duplicate camera resources and
 * ensure the preview layer and photo capture are coordinated.
 */
@OptIn(ExperimentalForeignApi::class)
internal object IosCameraSession {
    val session = AVCaptureSession()
    val sessionQueue: dispatch_queue_t =
        dispatch_queue_create("com.scandoc.camera.session", null)

    private var hasVideoInput = false

    fun start(photoOutput: AVCapturePhotoOutput? = null) {
        dispatch_async(sessionQueue) {
            configure(photoOutput)
            if (!session.isRunning()) {
                session.startRunning()
            }
        }
    }

    fun stop() {
        dispatch_async(sessionQueue) {
            if (session.isRunning()) {
                session.stopRunning()
            }
        }
    }

    fun onSessionQueue(block: () -> Unit) {
        dispatch_async(sessionQueue) {
            block()
        }
    }

    @OptIn(BetaInteropApi::class)
    fun configure(photoOutput: AVCapturePhotoOutput? = null) {
        if (hasVideoInput && (photoOutput == null || session.outputs.contains(photoOutput))) {
            return
        }

        session.beginConfiguration()
        session.sessionPreset = AVCaptureSessionPresetPhoto

        if (!hasVideoInput) {
            hasVideoInput = addBackCameraInput()
        }

        if (photoOutput != null && !session.outputs.contains(photoOutput) && session.canAddOutput(photoOutput)) {
            session.addOutput(photoOutput)
        }

        session.commitConfiguration()
    }

    @OptIn(BetaInteropApi::class)
    private fun addBackCameraInput(): Boolean {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo) ?: return false
        return memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val input = AVCaptureDeviceInput(device = device, error = errorPtr.ptr)
            session.canAddInput(input) && run {
                session.addInput(input)
                true
            }
        }
    }
}
