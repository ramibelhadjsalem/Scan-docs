package com.scandoc.core.platform

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class PlatformOcrEngine actual constructor() : OcrEngine {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun recognize(imageBytes: ByteArray): OcrResult = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ?: return@withContext OcrResult(fullText = "", blocks = emptyList(), confidence = 0f, language = null)

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        suspendCancellableCoroutine { cont ->
            recognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    val blocks = visionText.textBlocks.map { block ->
                        val box = block.boundingBox
                        OcrBlock(
                            text = block.text,
                            left = box?.left ?: 0,
                            top = box?.top ?: 0,
                            right = box?.right ?: 0,
                            bottom = box?.bottom ?: 0,
                            confidence = block.lines.mapNotNull { it.confidence }.average()
                                .takeIf { !it.isNaN() }?.toFloat() ?: 0.9f,
                        )
                    }
                    cont.resume(
                        OcrResult(
                            fullText = visionText.text,
                            blocks = blocks,
                            confidence = if (blocks.isEmpty()) 0f else 0.9f,
                            language = null,
                        )
                    )
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}
