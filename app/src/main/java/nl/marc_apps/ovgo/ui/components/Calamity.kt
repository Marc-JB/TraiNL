package nl.marc_apps.ovgo.ui.components

import android.text.format.DateUtils
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption

private val CalamityItemSpacing = 8.dp

@Composable
fun Calamity(calamity: DutchRailwaysDisruption.Calamity) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        Modifier
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Text(
            calamity.title,
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(
                if (calamity.priority == DutchRailwaysDisruption.Calamity.Priority.PRIO_1) {
                    R.color.sectionTitleWarningColor
                } else {
                    R.color.sectionTitleColor
                }
            )
        )

        Spacer(Modifier.height(CalamityItemSpacing))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            if (calamity.description != null) {
                Text(
                    calamity.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(CalamityItemSpacing))
            }

            if (calamity.lastUpdated != null) {
                Text(
                    DateUtils.getRelativeTimeSpanString(
                        calamity.lastUpdated.toEpochMilliseconds(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS
                    ).toString(),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
