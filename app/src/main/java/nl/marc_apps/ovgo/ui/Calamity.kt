package nl.marc_apps.ovgo.ui

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption

@Composable
fun Calamity(
    calamity: DutchRailwaysDisruption.Calamity,
    onCalamitySelected: (DutchRailwaysDisruption.Calamity) -> Unit
) {
    Column(
        Modifier
            .clickable {
                onCalamitySelected(calamity)
            }
            .padding(16.dp)
            .fillMaxWidth()
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

        Spacer(Modifier.height(8.dp))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            if (calamity.description != null) {
                Text(
                    calamity.description,
                    style = MaterialTheme.typography.body2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(8.dp))

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
