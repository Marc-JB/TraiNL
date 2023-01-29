package nl.marc_apps.ovgo.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest

@Composable
fun imageRequest(url: String, builder: @Composable ImageRequest.Builder.() -> Unit): ImageRequest {
    return ImageRequest.Builder(LocalContext.current)
        .data(url)
        .apply { builder() }
        .build()
}
