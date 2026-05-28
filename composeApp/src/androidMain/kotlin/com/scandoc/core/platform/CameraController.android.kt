package com.scandoc.core.platform

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.common.util.concurrent.ListenableFuture
import com.scandoc.domain.platform.CameraController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class PlatformCameraController(
    private val context: Context,
) : CameraController, LifecycleOwner {

    private var lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry

    private val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    private val analysisExecutor: Executor = Executors.newSingleThreadExecutor()

    private val _frames = MutableSharedFlow<ByteArray>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    actual override val frames: Flow<ByteArray> = _frames.asSharedFlow()

    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    actual override suspend fun start() = withContext(Dispatchers.Main) {
        if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
            lifecycleRegistry = LifecycleRegistry(this@PlatformCameraController)
        }
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val provider = ProcessCameraProvider.getInstance(context).await(mainExecutor)

        val capture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
        imageCapture = capture

        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .also { useCase ->
                useCase.setAnalyzer(analysisExecutor) { proxy ->
                    val bytes = proxy.toJpeg()
                    proxy.close()
                    _frames.tryEmit(bytes)
                }
            }

        camera = provider.bindToLifecycle(
            this@PlatformCameraController,
            CameraSelector.DEFAULT_BACK_CAMERA,
            capture,
            analysis,
        )
    }

    actual override suspend fun stop() = withContext(Dispatchers.Main) {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        imageCapture = null
        camera = null
    }

    actual override suspend fun capture(): ByteArray {
        val capture = imageCapture ?: return ByteArray(0)
        return suspendCancellableCoroutine { cont ->
            capture.takePicture(
                mainExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        val bytes = image.toJpeg()
                        image.close()
                        cont.resume(bytes)
                    }
                    override fun onError(exc: ImageCaptureException) {
                        cont.resumeWithException(exc)
                    }
                },
            )
        }
    }

    actual override suspend fun setFlash(enabled: Boolean) {
        camera?.cameraControl?.enableTorch(enabled)?.await(mainExecutor)
    }

    private fun ImageProxy.toJpeg(): ByteArray {
        if (format == android.graphics.ImageFormat.JPEG) {
            val buffer = planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            return bytes
        }
        val bitmap = toBitmap()
        val out = ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, out)
        bitmap.recycle()
        return out.toByteArray()
    }

    private suspend fun <T> ListenableFuture<T>.await(executor: Executor): T =
        suspendCancellableCoroutine { cont ->
            addListener({
                runCatching { get() }
                    .onSuccess { cont.resume(it) }
                    .onFailure { cont.resumeWithException(it) }
            }, executor)
            cont.invokeOnCancellation { cancel(true) }
        }
}
