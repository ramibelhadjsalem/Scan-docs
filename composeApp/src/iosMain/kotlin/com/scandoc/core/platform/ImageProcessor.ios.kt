package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter
import com.scandoc.domain.model.Offset
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.CIImage
import platform.CoreImage.CIVector
import platform.Foundation.NSError
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Vision.VNDetectRectanglesRequest
import platform.Vision.VNImageRequestHandler
import platform.Vision.VNRectangleObservation

@OptIn(ExperimentalForeignApi::class)
actual class PlatformImageProcessor() : com.scandoc.domain.platform.ImageProcessor {

    private val ciContext by lazy { CIContext.context() }

    actual override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? =
        withContext(Dispatchers.Default) {
            val ciImage = CIImage.imageWithData(imageBytes.toNSData())
                ?: return@withContext null

            var bestRect: VNRectangleObservation? = null
            val request = VNDetectRectanglesRequest { req, _ ->
                @Suppress("UNCHECKED_CAST")
                bestRect = (req?.results as? List<*>)
                    ?.filterIsInstance<VNRectangleObservation>()
                    ?.maxByOrNull { it.confidence }
            }
            request.minimumConfidence = 0.6f
            request.maximumObservations = 1

            memScoped {
                val error = alloc<ObjCObjectVar<NSError?>>()
                val handler = VNImageRequestHandler(CIImage = ciImage, options = emptyMap<Any?, Any?>())
                handler.performRequests(listOf(request), error.ptr)
            }

            val rect = bestRect ?: return@withContext null

            // Vision uses bottom-left origin; convert y to top-left origin for UI
            DocumentCorners(
                topLeft = Offset(
                    x = rect.topLeft.x.toFloat(),
                    y = (1.0 - rect.topLeft.y).toFloat(),
                ),
                topRight = Offset(
                    x = rect.topRight.x.toFloat(),
                    y = (1.0 - rect.topRight.y).toFloat(),
                ),
                bottomRight = Offset(
                    x = rect.bottomRight.x.toFloat(),
                    y = (1.0 - rect.bottomRight.y).toFloat(),
                ),
                bottomLeft = Offset(
                    x = rect.bottomLeft.x.toFloat(),
                    y = (1.0 - rect.bottomLeft.y).toFloat(),
                ),
            )
        }

    actual override suspend fun applyPerspective(
        imageBytes: ByteArray,
        corners: DocumentCorners,
    ): ByteArray = withContext(Dispatchers.Default) {
        val ciImage = CIImage.imageWithData(imageBytes.toNSData())
            ?: return@withContext imageBytes
        val w = ciImage.extent.size.width
        val h = ciImage.extent.size.height

        // CIImage uses bottom-left origin; UI corners use top-left origin
        fun ciVec(nx: Float, ny: Float): CIVector =
            CIVector.vectorWithX(nx * w, Y = (1.0 - ny) * h)

        val filter = CIFilter.filterWithName("CIPerspectiveCorrection")
            ?: return@withContext imageBytes
        filter.setValue(ciImage, forKey = "inputImage")
        filter.setValue(ciVec(corners.topLeft.x, corners.topLeft.y), forKey = "inputTopLeft")
        filter.setValue(ciVec(corners.topRight.x, corners.topRight.y), forKey = "inputTopRight")
        filter.setValue(ciVec(corners.bottomRight.x, corners.bottomRight.y), forKey = "inputBottomRight")
        filter.setValue(ciVec(corners.bottomLeft.x, corners.bottomLeft.y), forKey = "inputBottomLeft")

        val output = filter.outputImage ?: return@withContext imageBytes
        val cgImage = ciContext.createCGImage(output, fromRect = output.extent)
            ?: return@withContext imageBytes

        val uiImage = UIImage.imageWithCGImage(cgImage)
        UIImageJPEGRepresentation(uiImage, 0.92)?.toByteArray() ?: imageBytes
    }

    actual override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray =
        withContext(Dispatchers.Default) {
            if (filter == Filter.Original || filter == Filter.Photo || filter == Filter.Auto) {
                return@withContext imageBytes
            }
            val ciImage = CIImage.imageWithData(imageBytes.toNSData())
                ?: return@withContext imageBytes

            val ciFilterName = when (filter) {
                Filter.Grayscale -> "CIPhotoEffectTonal"
                Filter.BlackWhite -> "CIPhotoEffectNoir"
                Filter.Ink -> "CISepiaTone"
                Filter.Auto, Filter.Photo, Filter.Original -> return@withContext imageBytes
            }

            val ciFilter = CIFilter.filterWithName(ciFilterName)
                ?: return@withContext imageBytes
            ciFilter.setValue(ciImage, forKey = "inputImage")
            if (filter == Filter.Ink) {
                ciFilter.setValue(0.7, forKey = "inputIntensity")
            }

            val output = ciFilter.outputImage ?: return@withContext imageBytes
            val cgImage = ciContext.createCGImage(output, fromRect = ciImage.extent)
                ?: return@withContext imageBytes

            val uiImage = UIImage.imageWithCGImage(cgImage)
            UIImageJPEGRepresentation(uiImage, 0.92)?.toByteArray() ?: imageBytes
        }
}
