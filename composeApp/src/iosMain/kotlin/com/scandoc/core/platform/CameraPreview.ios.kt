package com.scandoc.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVCaptureSession
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CALayer
import platform.UIKit.UIView
import platform.UIKit.UIColor

/**
 * iOS actual: embeds a UIView with a live [AVCaptureVideoPreviewLayer].
 *
 * Uses the shared [IosCameraSession] so preview and controller coordinate
 * on a single [AVCaptureSession]. The session is started on a background
 * queue; the preview layer is sized in the [update] callback.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(
    modifier: Modifier,
) {
    UIKitView(
        factory = {
            IosCameraSession.start()
            CameraPreviewView(IosCameraSession.session)
        },
        update = { view ->
            view.layoutPreviewLayer()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalForeignApi::class)
private class CameraPreviewView(
    session: AVCaptureSession,
) : UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    private val previewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
        videoGravity = AVLayerVideoGravityResizeAspectFill
    }

    init {
        backgroundColor = UIColor.blackColor
        opaque = true
        clipsToBounds = true
        layer.addSublayer(previewLayer as CALayer)
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        layoutPreviewLayer()
    }

    fun layoutPreviewLayer() {
        previewLayer.setFrame(bounds)
    }
}
