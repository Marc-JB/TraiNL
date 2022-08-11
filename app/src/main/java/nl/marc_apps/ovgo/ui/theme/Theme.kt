package nl.marc_apps.ovgo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = BluePrimaryLight,
    primaryVariant = BluePrimary,
    secondary = YellowAccentLight,
    background = Gray900,
    surface = Gray850
)

private val LightColorPalette = lightColors(
    primary = BluePrimary,
    primaryVariant = BluePrimaryDark,
    secondary = YellowAccent,
    background = Gray50,
    surface = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = TextAppearance,
        content = content
    )
}
