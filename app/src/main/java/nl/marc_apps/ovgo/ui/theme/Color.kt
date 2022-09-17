package nl.marc_apps.ovgo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BluePrimaryLight = Color(0xFF63A4FF)
val BluePrimary = Color(0xFF1976D2)
val BluePrimaryDark = Color(0xFF004BA0)

val YellowAccentLight = Color(0xFFFFFF72)
val YellowAccent = Color(0xFFFFEB3B)
val YellowAccentDark = Color(0xFFC8B900)

val Gray50 = Color(0xFFFAFAFA)
val Gray850 = Color(0xFF2D2D2D)
val Gray900 = Color(0xFF212121)

val SubtitleColor
    @Composable
    get() = if(isSystemInDarkTheme()) BluePrimaryLight else BluePrimary
