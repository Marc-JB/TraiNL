package nl.marc_apps.ovgo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val PlatformViewCornerRadius = 2.dp
private val PlatformViewElevation = 4.dp

@Composable
fun PlatformView(
    modifier: Modifier = Modifier,
    whiteSquareSize: Dp = 10.dp,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable BoxScope.() -> Unit
) {
    Box (
        modifier = modifier
            .shadow(PlatformViewElevation, RoundedCornerShape(PlatformViewCornerRadius))
            .background(
                backgroundColor,
                RoundedCornerShape(
                    topStart = PlatformViewCornerRadius + 2.dp,
                    PlatformViewCornerRadius,
                    PlatformViewCornerRadius,
                    PlatformViewCornerRadius
                )
            )
    ){
        Box (
            modifier = Modifier
                .size(whiteSquareSize)
                .background(Color.White, RoundedCornerShape(topStart = PlatformViewCornerRadius))
                .align(Alignment.TopStart)
        ){}

        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}
