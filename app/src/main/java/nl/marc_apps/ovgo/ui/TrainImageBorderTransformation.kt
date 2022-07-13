package nl.marc_apps.ovgo.ui

import android.graphics.*
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class TrainImageBorderTransformation(
    key: String,
    private val strokeWidth: Int = 2,
    private val color: Int = Color.WHITE
) : Transformation {
    override val cacheKey = "train-image-border:$key:$strokeWidth:$color"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val inputWithRemovedBackground = changeBitmapAlpha(changeBitmapAlpha(input, 0.1f), 10f)

        val doubleStrokeWidth = 2 * strokeWidth

        return createBitmap(input.width + doubleStrokeWidth, input.height + doubleStrokeWidth).applyCanvas {
            val range = 0..doubleStrokeWidth
            for (index in range) {
                drawBitmap(inputWithRemovedBackground, 0f, index.toFloat(), null)
                if (index != 0) {
                    drawBitmap(inputWithRemovedBackground, index.toFloat(), 0f, null)
                }
                drawBitmap(inputWithRemovedBackground, doubleStrokeWidth.toFloat(), index.toFloat(), null)
                if (index != doubleStrokeWidth) {
                    drawBitmap(inputWithRemovedBackground, index.toFloat(), doubleStrokeWidth.toFloat(), null)
                }
            }
            drawColor(color, PorterDuff.Mode.SRC_ATOP)
            drawBitmap(inputWithRemovedBackground, strokeWidth.toFloat(), strokeWidth.toFloat(), null)
        }
    }

    private fun changeBitmapAlpha(bitmap: Bitmap, factor: Float): Bitmap {
        return createBitmap(bitmap.width, bitmap.height).applyCanvas {
            drawBitmap(bitmap, 0f, 0f, Paint().apply {
                this.colorFilter = ColorMatrixColorFilter(floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, factor, 0f
                ))
            })
        }
    }
}
