package nl.marc_apps.ovgo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BluePrimaryLight,
    primaryVariant = BluePrimary,
    secondary = YellowAccentLight,
    background = Gray900,
    surface = Gray800
)

private val LightColorPalette = lightColors(
    primary = BluePrimary,
    primaryVariant = BluePrimaryDark,
    secondary = YellowAccent,
    background = Gray50,
    surface = Gray200
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
