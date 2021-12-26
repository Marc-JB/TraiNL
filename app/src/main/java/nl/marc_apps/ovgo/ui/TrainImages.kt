package nl.marc_apps.ovgo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import coil.ImageLoader
import coil.load
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.PartialTrainImageBinding

object TrainImages {
    private fun isImageUrlFromWhiteTrain(imageUrl: String): Boolean {
        return "ice" in imageUrl
    }

    fun loadView(root: ViewGroup, imageUrls: List<String>?, imageLoader: ImageLoader) {
        val shouldDrawImageBorder = root.resources.getBoolean(R.bool.should_draw_train_image_border)
        val trainImageStrokeWidth = root.resources.getDimensionPixelSize(R.dimen.train_image_stroke_width)

        if (shouldDrawImageBorder || imageUrls?.any { isImageUrlFromWhiteTrain(it) } == true) {
            val trainImageHeight = root.resources.getDimensionPixelSize(R.dimen.train_image_height)
            (root.parent as? ViewGroup)?.updateLayoutParams {
                height = trainImageHeight + trainImageStrokeWidth * 2
            }
        }

        imageUrls?.forEach {
            val imageView = PartialTrainImageBinding.inflate(
                LayoutInflater.from(root.context),
                root,
                true
            )

            val isWhiteTrain = isImageUrlFromWhiteTrain(it)

            val borderColor = if (isWhiteTrain) R.color.trainImageBorderAlternative else R.color.trainImageBorder
            val borderTransformation = TrainImageBorderTransformation(
                key = it,
                trainImageStrokeWidth,
                ContextCompat.getColor(imageView.root.context, borderColor)
            )
            imageView.root.load(it, imageLoader) {
                if (isWhiteTrain || shouldDrawImageBorder) {
                    transformations(borderTransformation)
                }
            }
        }
    }
}
