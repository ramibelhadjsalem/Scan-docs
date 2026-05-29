package com.scandoc.core.platform

import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFDataCreate
import platform.ImageIO.CGImageSourceCreateImageAtIndex
import platform.ImageIO.CGImageSourceCreateWithData
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRecognizedTextObservation
import platform.Vision.VNRequestTextRecognitionLevelAccurate

/**
 * iOS actual — uses Vision VNRecognizeTextRequest for on-device OCR.
 * Requires iOS 13+ for accurate recognition level.
 */
@OptIn(ExperimentalForeignApi::class)
actual class PlatformOcrEngine actual constructor() : com.scandoc.domain.platform.OcrEngine {

    actual override suspend fun recognize(imageBytes: ByteArray): OcrResult {
        if (imageBytes.isEmpty()) return emptyResult()

        val cgImage = imageBytes.toCGImage() ?: return emptyResult()
        val handler = VNImageRequestHandler(cGImage = cgImage, options = emptyMap<Any?, Any?>())
        val request = VNRecognizeTextRequest()
        request.recognitionLevel = VNRequestTextRecognitionLevelAccurate
        request.usesLanguageCorrection = true

        val requestPerformed = handler.performRequests(listOf(request), null)
        if (!requestPerformed) return emptyResult()

        @Suppress("UNCHECKED_CAST")
        val observations = (request.results as? List<VNRecognizedTextObservation>).orEmpty()

        val blocks = observations.mapNotNull { obs ->
            @Suppress("UNCHECKED_CAST")
            val candidates = obs.topCandidates(1u) as? List<platform.Vision.VNRecognizedText>
            val top = candidates?.firstOrNull() ?: return@mapNotNull null
            val box = obs.boundingBox.useContents {
                BoundingBox(
                    x = origin.x,
                    y = origin.y,
                    width = size.width,
                    height = size.height,
                )
            }
            OcrBlock(
                text = top.string,
                confidence = top.confidence,
                left = (box.x * 1000).toInt(),
                top = ((1.0 - box.y - box.height) * 1000).toInt(),
                right = ((box.x + box.width) * 1000).toInt(),
                bottom = ((1.0 - box.y) * 1000).toInt(),
            )
        }

        val fullText = blocks.joinToString("\n") { it.text }
        val avgConfidence = if (blocks.isEmpty()) 0f
        else blocks.map { it.confidence.toDouble() }.average().toFloat()

        return OcrResult(
            fullText = fullText,
            blocks = blocks,
            confidence = avgConfidence,
            language = null,
        )
    }

    private fun emptyResult() = OcrResult(fullText = "", blocks = emptyList(), confidence = 0f, language = null)

    private fun ByteArray.toCGImage() =
        if (isEmpty()) {
            null
        } else {
            val data = usePinned { pinned ->
                CFDataCreate(null, pinned.addressOf(0).reinterpret(), size.toLong())
            } ?: return null
            val source = CGImageSourceCreateWithData(data, null) ?: return null
            CGImageSourceCreateImageAtIndex(source, 0u, null)
        }

    private data class BoundingBox(
        val x: Double,
        val y: Double,
        val width: Double,
        val height: Double,
    )
}
