package nl.marc_apps.ovgo.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.ui.imageRequest
import nl.marc_apps.ovgo.ui.TrainImageBorderTransformation

private fun isImageUrlFromWhiteTrain(imageUrl: String): Boolean {
    return "ice" in imageUrl
}

@Composable
fun TrainImagesView(
    imageUrls: List<String>,
    paddingStart: Dp,
    paddingEnd: Dp,
    imageLoader: ImageLoader? = null,
    header: (@Composable () -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        if (header != null) {
            Box (
                modifier = Modifier.widthIn(min = paddingStart)
            ) {
                header()
            }
        }

        Row {
            for (index in imageUrls.indices) {
                if (index == 0 && header == null) {
                    Spacer(Modifier.width(paddingStart))
                }

                TrainImage(imageUrls[index], imageLoader)

                if (index == imageUrls.lastIndex) {
                    Spacer(Modifier.width(paddingEnd))
                }
            }
        }
    }
}

@Composable
fun TrainImage(url: String, imageLoader: ImageLoader? = null) {
    val shouldDrawImageBorder = booleanResource(R.bool.should_draw_train_image_border)
    val trainImageStrokeWidth = dimensionResource(R.dimen.train_image_stroke_width)
    val trainImageHeight = dimensionResource(R.dimen.train_image_height)

    val isWhiteTrain = isImageUrlFromWhiteTrain(url)

    val borderColor = if (isWhiteTrain) R.color.trainImageBorderAlternative else R.color.trainImageBorder
    val borderTransformation = TrainImageBorderTransformation(
        key = url,
        with(LocalDensity.current) { trainImageStrokeWidth.roundToPx() },
        colorResource(borderColor)
    )

    val shouldApplyBorder = isWhiteTrain || shouldDrawImageBorder

    AsyncImage(
        imageRequest(url = url) {
            if (shouldApplyBorder) {
                transformations(borderTransformation)
            }
        },
        imageLoader = imageLoader ?: LocalContext.current.imageLoader,
        contentScale = ContentScale.Fit,
        contentDescription = "Train image",
        modifier = Modifier.height(
            if (shouldApplyBorder) {
                trainImageHeight + trainImageStrokeWidth * 2
            } else {
                trainImageHeight
            }
        )
    )
}
