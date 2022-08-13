package nl.marc_apps.ovgo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption

@Composable
fun DisruptionsList(
    disruptions: List<DutchRailwaysDisruption>
) {
    LazyColumn {
        itemsIndexed(
            disruptions,
            key = { _, disruption -> disruption.id },
            contentType = { _, disruption ->
                when (disruption) {
                    is DutchRailwaysDisruption.DisruptionOrMaintenance -> 1
                    is DutchRailwaysDisruption.Calamity -> 0
                }
            }
        ) { index, disruption ->
            when (disruption) {
                is DutchRailwaysDisruption.DisruptionOrMaintenance -> Disruption(disruption)
                is DutchRailwaysDisruption.Calamity -> Calamity(disruption)
            }

            if (index != disruptions.lastIndex) {
                Divider(Modifier.padding(16.dp, 0.dp))
            } else {
                Spacer(Modifier.height(56.dp))
            }
        }
    }
}
