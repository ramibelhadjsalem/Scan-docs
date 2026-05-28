package com.scandoc.core.platform

import com.scandoc.domain.model.OcrBlock
import com.scandoc.domain.model.OcrResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreImage.CIImage
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRecognizeTextObservation
import platform.Vision.VNRecognizeTextRequest
import platform.Vision.VNRequestTextRecognitionLevelAccurate

@OptIn(ExperimentalForeignApi::class)
actual class PlatformOcrEngine() : com.scandoc.domain.platform.OcrEngine {

    actual override suspend fun recognize(imageBytes: ByteArray): OcrResult =
        withContext(Dispatchers.Default) {
            val ciImage = CIImage.imageWithData(imageBytes.toNSData())
                ?: return@withContext emptyResult()

            var recognizedObservations: List<VNRecognizedTextObservation> = emptyList()
            val request = VNRecognizeTextRequest { req, _ ->
                @Suppress("UNCHECKED_CAST")
                recognizedObservations =
                    (req?.results as? List<*>)
                        ?.filterIsInstance<VNRecognizedTextObservation>()
                        ?: emptyList()
            }
            request.recognitionLevel = VNRequestTextRecognitionLevelAccurate
            request.usesLanguageCorrection = true

            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                val handler = VNImageRequestHandler(CIImage = ciImage, options = emptyMap<Any?, Any?>())
                handler.performRequests(listOf(request), error.ptr)
            }

            val blocks = recognizedObservations.mapNotNull { obs ->
                val candidate = obs.topCandidates(1).firstOrNull() ?: return@mapNotNull null
                val box = obs.boundingBox
                OcrBlock(
                    text = candidate.string,
                    left = (box.origin.x * 1000).toInt(),
                    top = ((1.0 - box.origin.y - box.size.height) * 1000).toInt(),
                    right = ((box.origin.x + box.size.width) * 1000).toInt(),
                    bottom = ((1.0 - box.origin.y) * 1000).toInt(),
                    confidence = candidate.confidence,
                )
            }
            val fullText = blocks.joinToString("\n") { it.text }
            val avgConf = blocks.map { it.confidence }.average().toFloat()
                .takeIf { !it.isNaN() } ?: 0f

            OcrResult(fullText = fullText, blocks = blocks, confidence = avgConf, language = null)
        }

    private fun emptyResult() =
        OcrResult(fullText = "", blocks = emptyList(), confidence = 0f, language = null)
}

@OptIn(ExperimentalForeignApi::class)
internal fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
}

@OptIn(ExperimentalForeignApi::class)
internal fun NSData.toByteArray(): ByteArray {
    val len = length.toInt()
    if (len == 0) return ByteArray(0)
    return ByteArray(len).also { arr ->
        arr.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), bytes, length)
        }
    }
}
