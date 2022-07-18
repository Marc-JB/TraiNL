package nl.marc_apps.ovgo.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TextAppearance
    @Composable
    get() = MaterialTheme.typography.copy(
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
