package com.scandoc.core.platform

import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
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

        val nsData: NSData = imageBytes.usePinned { pinned ->
            NSData(bytes = pinned.addressOf(0), length = imageBytes.size.toULong())
        }

        val handler = VNImageRequestHandler(data = nsData, options = emptyMap<Any?, Any?>())
        val request = VNRecognizeTextRequest()
        request.recognitionLevel = VNRequestTextRecognitionLevelAccurate
        request.usesLanguageCorrection = true

        runCatching { handler.performRequests(listOf(request), null) }

        @Suppress("UNCHECKED_CAST")
        val observations = (request.results as? List<VNRecognizedTextObservation>).orEmpty()

        val blocks = observations.mapNotNull { obs ->
            @Suppress("UNCHECKED_CAST")
            val candidates = obs.topCandidates(1u) as? List<platform.Vision.VNRecognizedText>
            val top = candidates?.firstOrNull() ?: return@mapNotNull null
            val box = obs.boundingBox
            OcrBlock(
                text = top.string,
                confidence = top.confidence,
                left = (box.origin.x * 1000).toInt(),
                top = ((1.0 - box.origin.y - box.size.height) * 1000).toInt(),
                right = ((box.origin.x + box.size.width) * 1000).toInt(),
                bottom = ((1.0 - box.origin.y) * 1000).toInt(),
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
}
