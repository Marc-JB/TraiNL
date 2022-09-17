package nl.marc_apps.ovgo.ui

import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

class TrainImageBorderTransformation(
    key: String,
    private val strokeWidth: Int = 2,
    private val color: Color = Color(android.graphics.Color.WHITE)
) : Transformation {
    override val cacheKey = "train-image-border:$key:$strokeWidth:$color"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val inputWithRemovedBackground = changeBitmapAlpha(changeBitmapAlpha(input, 0.1f), 10f)
        val whiteBitmap = changeBitmapColor(inputWithRemovedBackground)

        val doubleStrokeWidth = 2 * strokeWidth

        return createBitmap(input.width + doubleStrokeWidth, input.height + doubleStrokeWidth).applyCanvas {
            val range = 0..doubleStrokeWidth
            for (index in range) {
                drawBitmap(whiteBitmap, 0f, index.toFloat(), null)
                if (index != 0) {
                    drawBitmap(whiteBitmap, index.toFloat(), 0f, null)
                }
                drawBitmap(whiteBitmap, doubleStrokeWidth.toFloat(), index.toFloat(), null)
                if (index != doubleStrokeWidth) {
                    drawBitmap(whiteBitmap, index.toFloat(), doubleStrokeWidth.toFloat(), null)
                }
            }

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

    private fun changeBitmapColor(bitmap: Bitmap): Bitmap {
        return createBitmap(bitmap.width, bitmap.height).applyCanvas {
            drawBitmap(bitmap, 0f, 0f, Paint().apply {
                this.colorFilter = ColorMatrixColorFilter(floatArrayOf(
                    0f, 0f, 0f, 0f, this@TrainImageBorderTransformation.color.red * 255f,
                    0f, 0f, 0f, 0f, this@TrainImageBorderTransformation.color.green * 255f,
                    0f, 0f, 0f, 0f, this@TrainImageBorderTransformation.color.blue * 255f,
                    0f, 0f, 0f, 1f, 0f
                ))
            })
        }
    }
}
