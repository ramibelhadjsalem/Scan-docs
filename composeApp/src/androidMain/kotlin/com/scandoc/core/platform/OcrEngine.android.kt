package com.scandoc.core.platform

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

actual class PlatformOcrEngine actual constructor() : com.scandoc.domain.platform.OcrEngine {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    actual override suspend fun recognize(imageBytes: ByteArray): OcrResult {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ?: error("Unable to decode image for OCR")
        val image = InputImage.fromBitmap(bitmap, 0)
        val text = recognizer.process(image).await()
        bitmap.recycle()
        return text.toOcrResult()
    }

    private fun Text.toOcrResult(): OcrResult {
        val blocks = textBlocks.mapNotNull { block ->
            val box = block.boundingBox ?: return@mapNotNull null
            OcrBlock(
                text = block.text,
                left = box.left,
                top = box.top,
                right = box.right,
                bottom = box.bottom,
                confidence = 1f,
            )
        }
        return OcrResult(
            fullText = text,
            blocks = blocks,
            confidence = if (blocks.isEmpty()) 0f else blocks.map { it.confidence }.average().toFloat(),
            language = null,
        )
    }

    private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
        suspendCancellableCoroutine { continuation ->
            addOnSuccessListener { value -> continuation.resume(value) }
            addOnFailureListener { throwable -> continuation.resumeWithException(throwable) }
            addOnCanceledListener { continuation.cancel() }
        }
}
