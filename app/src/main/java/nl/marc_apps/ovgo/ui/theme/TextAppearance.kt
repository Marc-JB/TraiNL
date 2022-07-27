package nl.marc_apps.ovgo.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.marc_apps.ovgo.R

val TextAppearance
    @Composable
    get() = Typography(
        defaultFontFamily = FontFamily(
            Font(R.font.roboto_regular, FontWeight.Normal),
            Font(R.font.roboto_bold, FontWeight.Bold),
        ),
        subtitle1 = MaterialTheme.typography.subtitle1.copy(
            fontWeight = FontWeight.Bold
        ),
        caption = MaterialTheme.typography.caption.copy(
            fontSize = 14.sp
        ),
        overline = MaterialTheme.typography.caption.copy(
            fontSize = 12.sp
        )
    )
