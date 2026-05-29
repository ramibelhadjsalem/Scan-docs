package com.scandoc.core.platform

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

/**
 * Android actual: embeds a CameraX [PreviewView] with a live camera preview.
 *
 * The preview is self-contained: it requests a [ProcessCameraProvider],
 * builds a [Preview] use case, and binds it to the current back-facing camera.
 * When the composable leaves composition the lifecycle owner destruction
 * automatically unbinds the camera.
 */
@Composable
actual fun CameraPreview(
    modifier: Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = cameraPreviewImplementationMode()
            }
        },
        modifier = modifier,
    ) { view ->
        previewView = view
    }
    CameraPreviewBinding(
        previewView = previewView,
        lifecycleOwner = lifecycleOwner,
    )
}

@Composable
private fun CameraPreviewBinding(
    previewView: PreviewView?,
    lifecycleOwner: LifecycleOwner,
) {
    DisposableEffect(previewView, lifecycleOwner) {
        val view = previewView ?: return@DisposableEffect onDispose {}
        val cameraProviderFuture = ProcessCameraProvider.getInstance(view.context)
        cameraProviderFuture.addListener(
            {
                bindCameraPreview(
                    cameraProviderFuture = cameraProviderFuture,
                    previewView = view,
                    lifecycleOwner = lifecycleOwner,
                )
            },
            ContextCompat.getMainExecutor(view.context),
        )

        onDispose {
            unbindCameraPreview(cameraProviderFuture)
        }
    }
}

internal fun cameraPreviewImplementationMode(): PreviewView.ImplementationMode =
    PreviewView.ImplementationMode.COMPATIBLE

private fun bindCameraPreview(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
) {
    try {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder()
            .build()
            .also { preview ->
                preview.setSurfaceProvider(previewView.surfaceProvider)
            }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
        )
    } catch (_: ExecutionException) {
        // Camera provider failed to initialize.
    } catch (_: InterruptedException) {
        Thread.currentThread().interrupt()
    } catch (_: IllegalArgumentException) {
        // No compatible camera or invalid lifecycle binding.
    } catch (_: IllegalStateException) {
        // Lifecycle/camera provider state changed while binding.
    }
}

private fun unbindCameraPreview(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
) {
    if (!cameraProviderFuture.isDone) return
    try {
        cameraProviderFuture.get().unbindAll()
    } catch (_: ExecutionException) {
        // Provider initialization already failed; nothing to unbind.
    } catch (_: InterruptedException) {
        Thread.currentThread().interrupt()
    } catch (_: IllegalStateException) {
        // Provider is no longer in a state where unbind is useful.
    }
}
