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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual class PlatformImageProcessor actual constructor() : ImageProcessor {

    override suspend fun detectCorners(imageBytes: ByteArray): DocumentCorners? = null

    override suspend fun applyPerspective(
        imageBytes: ByteArray,
        corners: DocumentCorners,
    ): ByteArray = withContext(Dispatchers.IO) {
        val src = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ?: return@withContext imageBytes

        val w = src.width.toFloat()
        val h = src.height.toFloat()
        val dstWidth = 1080
        val dstHeight = 1440

        val matrix = Matrix()
        matrix.setPolyToPoly(
            floatArrayOf(
                corners.topLeft.x * w, corners.topLeft.y * h,
                corners.topRight.x * w, corners.topRight.y * h,
                corners.bottomRight.x * w, corners.bottomRight.y * h,
                corners.bottomLeft.x * w, corners.bottomLeft.y * h,
            ),
            0,
            floatArrayOf(
                0f, 0f,
                dstWidth.toFloat(), 0f,
                dstWidth.toFloat(), dstHeight.toFloat(),
                0f, dstHeight.toFloat(),
            ),
            0,
            4,
        )

        val result = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        Canvas(result).drawBitmap(src, matrix, Paint(Paint.FILTER_BITMAP_FLAG))
        src.recycle()

        val out = ByteArrayOutputStream()
        result.compress(Bitmap.CompressFormat.JPEG, 90, out)
        result.recycle()
        out.toByteArray()
    }

    override suspend fun applyFilter(imageBytes: ByteArray, filter: Filter): ByteArray =
        withContext(Dispatchers.IO) {
            val src = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: return@withContext imageBytes

            val result = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            val paint = Paint()
            paint.colorFilter = ColorMatrixColorFilter(filter.toColorMatrix())
            canvas.drawBitmap(src, 0f, 0f, paint)
            src.recycle()

            val out = ByteArrayOutputStream()
            result.compress(Bitmap.CompressFormat.JPEG, 90, out)
            result.recycle()
            out.toByteArray()
        }
}

private fun Filter.toColorMatrix(): ColorMatrix = when (this) {
    Filter.BlackWhite -> ColorMatrix().also { it.setSaturation(0f) }
    Filter.Grayscale -> ColorMatrix().also { it.setSaturation(0.15f) }
    Filter.Ink -> ColorMatrix(
        floatArrayOf(
            2.0f, 0f, 0f, 0f, -80f,
            0f, 2.0f, 0f, 0f, -80f,
            0f, 0f, 2.0f, 0f, -80f,
            0f, 0f, 0f, 1f, 0f,
        )
    ).also { bw -> ColorMatrix().also { it.setSaturation(0f) }.let { bw.postConcat(it) } }
    Filter.Auto -> ColorMatrix(
        floatArrayOf(
            1.1f, 0f, 0f, 0f, -12f,
            0f, 1.1f, 0f, 0f, -12f,
            0f, 0f, 1.1f, 0f, -12f,
            0f, 0f, 0f, 1f, 0f,
        )
    )
    Filter.Photo, Filter.Original -> ColorMatrix()
}
