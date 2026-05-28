package com.scandoc.core.platform

import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter
import com.scandoc.domain.model.Offset
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.CIImage
import platform.CoreImage.filterWithName
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

/**
 * iOS actual — uses CoreImage for filter application and perspective correction.
 * Corner detection returns a full-image bounding box; real edge detection would
 * require a VisionKit/OpenCV integration outside this scope.
 */
@OptIn(ExperimentalForeignApi::class)
actual class PlatformImageProcessor actual constructor() : com.scandoc.domain.platform.ImageProcessor {

    actual override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? {
        val uiImage = imageBytes.toUIImage() ?: return null
        val w = uiImage.size.useContents { width }.toFloat()
        val h = uiImage.size.useContents { height }.toFloat()
        return DocumentCorners(
            topLeft = Offset(0f, 0f),
            topRight = Offset(w, 0f),
            bottomRight = Offset(w, h),
            bottomLeft = Offset(0f, h),
        )
    }

    actual override suspend fun applyPerspective(
        imageBytes: ByteArray,
        corners: DocumentCorners,
    ): ByteArray {
        val ciImage = imageBytes.toCIImage() ?: return imageBytes

        val filter = CIFilter.filterWithName("CIPerspectiveCorrection") ?: return imageBytes
        filter.setValue(ciImage, forKey = "inputImage")

        val h = ciImage.extent.useContents { size.height }
        filter.setValue(ciVector(corners.topLeft.x, h - corners.topLeft.y), forKey = "inputTopLeft")
        filter.setValue(ciVector(corners.topRight.x, h - corners.topRight.y), forKey = "inputTopRight")
        filter.setValue(ciVector(corners.bottomRight.x, h - corners.bottomRight.y), forKey = "inputBottomRight")
        filter.setValue(ciVector(corners.bottomLeft.x, h - corners.bottomLeft.y), forKey = "inputBottomLeft")

        return filter.outputImage?.toJpegBytes() ?: imageBytes
    }

    actual override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray {
        if (filter == Filter.Original) return imageBytes
        val ciImage = imageBytes.toCIImage() ?: return imageBytes

        val ciFilter: CIFilter? = when (filter) {
            Filter.BlackWhite -> CIFilter.filterWithName("CIPhotoEffectNoir")
            Filter.Grayscale -> CIFilter.filterWithName("CIPhotoEffectMono")
            Filter.Ink -> CIFilter.filterWithName("CIPhotoEffectProcess")
            Filter.Auto -> CIFilter.filterWithName("CIColorControls")?.apply {
                setValue(1.1, forKey = "inputContrast")
                setValue(0.0, forKey = "inputSaturation")
            }
            Filter.Photo -> CIFilter.filterWithName("CIPhotoEffectChrome")
            Filter.Original -> null
        }

        ciFilter?.setValue(ciImage, forKey = "inputImage")
        return ciFilter?.outputImage?.toJpegBytes() ?: imageBytes
    }

    private fun ByteArray.toUIImage(): UIImage? {
        if (isEmpty()) return null
        val nsData: NSData = usePinned { pinned ->
            NSData(bytes = pinned.addressOf(0), length = size.toULong())
        }
        return UIImage(data = nsData)
    }

    private fun ByteArray.toCIImage(): CIImage? {
        if (isEmpty()) return null
        val nsData: NSData = usePinned { pinned ->
            NSData(bytes = pinned.addressOf(0), length = size.toULong())
        }
        return CIImage(data = nsData)
    }

    private fun CIImage.toJpegBytes(): ByteArray? {
        val context = CIContext()
        val cgImage = context.createCGImage(this, this.extent) ?: return null
        val uiImage = UIImage(CGImage = cgImage)
        val nsData = UIImageJPEGRepresentation(uiImage, 0.92) ?: return null
        return nsData.toByteArray()
    }

    private fun ciVector(x: Float, y: Double): platform.CoreImage.CIVector =
        platform.CoreImage.CIVector(x = x.toDouble(), y = y)
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val pointer = bytes ?: return ByteArray(0)
    return ByteArray(length.toInt()).also { array ->
        array.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), pointer, length)
        }
    }
}
