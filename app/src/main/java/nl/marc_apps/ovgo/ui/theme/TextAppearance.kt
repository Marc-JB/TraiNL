package nl.marc_apps.ovgo.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.marc_apps.ovgo.R

object TextAppearance {
    val sectionTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.body2.merge(TextStyle(
            color = colorResource(R.color.sectionTitleColor),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ))

    val sectionTitleDisabled: TextStyle
        @Composable
        get() = sectionTitle.merge(TextStyle(
            color = colorResource(R.color.sectionTitleDisabledColor),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        ))

    val sectionTextMedium: TextStyle
        @Composable
        get() = MaterialTheme.typography.caption.merge(TextStyle(
            fontSize = 14.sp
        ))

    val sectionTextSmall: TextStyle
        @Composable
        get() = MaterialTheme.typography.caption.merge(TextStyle(
            fontSize = 12.sp
        ))
}
