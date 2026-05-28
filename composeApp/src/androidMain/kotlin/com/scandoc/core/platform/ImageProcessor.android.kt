package com.scandoc.core.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter
import com.scandoc.domain.model.Offset
import java.io.ByteArrayOutputStream
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.roundToInt

actual class PlatformImageProcessor actual constructor() : com.scandoc.domain.platform.ImageProcessor {

    actual override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? {
        imageBytes.decodeBitmap()?.recycle() ?: return null
        return DocumentCorners(
            topLeft = Offset(0.05f, 0.05f),
            topRight = Offset(0.95f, 0.05f),
            bottomRight = Offset(0.95f, 0.95f),
            bottomLeft = Offset(0.05f, 0.95f),
        )
    }

    actual override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray {
        val src = imageBytes.decodeBitmap() ?: return imageBytes
        val w = src.width.toFloat()
        val h = src.height.toFloat()

        val topW = hypot(
            (corners.topRight.x - corners.topLeft.x) * w,
            (corners.topRight.y - corners.topLeft.y) * h,
        )
        val botW = hypot(
            (corners.bottomRight.x - corners.bottomLeft.x) * w,
            (corners.bottomRight.y - corners.bottomLeft.y) * h,
        )
        val leftH = hypot(
            (corners.bottomLeft.x - corners.topLeft.x) * w,
            (corners.bottomLeft.y - corners.topLeft.y) * h,
        )
        val rightH = hypot(
            (corners.bottomRight.x - corners.topRight.x) * w,
            (corners.bottomRight.y - corners.topRight.y) * h,
        )

        val outW = max(topW, botW).roundToInt().coerceIn(100, 4096)
        val outH = max(leftH, rightH).roundToInt().coerceIn(100, 4096)

        val srcPts = floatArrayOf(
            corners.topLeft.x * w,     corners.topLeft.y * h,
            corners.topRight.x * w,    corners.topRight.y * h,
            corners.bottomRight.x * w, corners.bottomRight.y * h,
            corners.bottomLeft.x * w,  corners.bottomLeft.y * h,
        )
        val dstPts = floatArrayOf(
            0f,          0f,
            outW.toFloat(), 0f,
            outW.toFloat(), outH.toFloat(),
            0f,          outH.toFloat(),
        )

        val matrix = Matrix()
        matrix.setPolyToPoly(srcPts, 0, dstPts, 0, 4)

        val output = Bitmap.createBitmap(outW, outH, Bitmap.Config.ARGB_8888)
        Canvas(output).drawBitmap(src, matrix, Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
        src.recycle()

        return output.toJpegBytes().also { output.recycle() }
    }

    actual override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray {
        val bitmap = imageBytes.decodeBitmap() ?: return imageBytes
        if (filter == Filter.Original || filter == Filter.Photo || filter == Filter.Auto) {
            bitmap.recycle()
            return imageBytes
        }

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = when (filter) {
                Filter.BlackWhite -> ColorMatrixColorFilter(blackWhiteMatrix())
                Filter.Ink -> ColorMatrixColorFilter(inkMatrix())
                Filter.Grayscale -> ColorMatrixColorFilter(grayscaleMatrix())
                Filter.Auto, Filter.Photo, Filter.Original -> null
            }
        }
        Canvas(output).drawBitmap(bitmap, 0f, 0f, paint)
        bitmap.recycle()
        return output.toJpegBytes().also { output.recycle() }
    }

    private fun ByteArray.decodeBitmap(): Bitmap? =
        BitmapFactory.decodeByteArray(this, 0, size)

    private fun Bitmap.toJpegBytes(): ByteArray {
        val out = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 92, out)
        return out.toByteArray()
    }

    private fun grayscaleMatrix(): ColorMatrix =
        ColorMatrix().apply { setSaturation(0f) }

    private fun blackWhiteMatrix(): ColorMatrix =
        ColorMatrix(
            floatArrayOf(
                1.5f, 1.5f, 1.5f, 0f, -180f,
                1.5f, 1.5f, 1.5f, 0f, -180f,
                1.5f, 1.5f, 1.5f, 0f, -180f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )

    private fun inkMatrix(): ColorMatrix =
        ColorMatrix(
            floatArrayOf(
                0.9f, 0.3f, 0.2f, 0f, -20f,
                0.2f, 0.9f, 0.2f, 0f, -20f,
                0.1f, 0.2f, 1.1f, 0f, -10f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
}
