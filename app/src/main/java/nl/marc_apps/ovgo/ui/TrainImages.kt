package nl.marc_apps.ovgo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.PartialTrainImageBinding

object TrainImages {
    fun loadView(root: ViewGroup, imageUrls: List<String>?) {
        val shouldDrawImageBorder = root.resources.getBoolean(R.bool.should_draw_train_image_border)

        imageUrls?.forEach {
            val imageView = PartialTrainImageBinding.inflate(
                LayoutInflater.from(root.context),
                root,
                true
            )

            val isIce = "ice" in it

            val borderColor = if (isIce) R.color.trainImageBorderAlternative else R.color.trainImageBorder
            val borderTransformation = TrainImageBorderTransformation(
                key = it,
                imageView.root.resources.getDimensionPixelSize(R.dimen.train_image_stroke_width),
                imageView.root.context.getColor(borderColor)
            )
            imageView.root.load(it) {
                if (isIce || shouldDrawImageBorder) {
                    transformations(borderTransformation)
                }
            }
        }
    }
}
