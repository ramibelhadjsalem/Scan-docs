package com.scandoc.core.platform

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.scandoc.domain.model.DocumentCorners
import com.scandoc.domain.model.Filter
import com.scandoc.domain.model.Offset
import java.io.ByteArrayOutputStream

actual class PlatformImageProcessor actual constructor() : com.scandoc.domain.platform.ImageProcessor {
    actual override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? {
        val bitmap = imageBytes.decodeBitmap() ?: return null
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()
        bitmap.recycle()
        return DocumentCorners(
            topLeft = Offset(0f, 0f),
            topRight = Offset(width, 0f),
            bottomRight = Offset(width, height),
            bottomLeft = Offset(0f, height),
        )
    }

    actual override suspend fun applyPerspective(imageBytes: ByteArray, corners: DocumentCorners): ByteArray =
        imageBytes

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
                Filter.Auto,
                Filter.Photo,
                Filter.Original,
                -> null
            }
        }
        Canvas(output).drawBitmap(bitmap, 0f, 0f, paint)
        bitmap.recycle()
        return output.toJpegBytes()
    }

    private fun ByteArray.decodeBitmap(): Bitmap? =
        BitmapFactory.decodeByteArray(this, 0, size)

    private fun Bitmap.toJpegBytes(): ByteArray {
        val output = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 92, output)
        return output.toByteArray()
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
