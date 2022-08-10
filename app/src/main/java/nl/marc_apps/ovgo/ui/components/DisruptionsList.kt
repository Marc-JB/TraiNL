package nl.marc_apps.ovgo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption

@Composable
fun DisruptionsList(
    disruptions: List<DutchRailwaysDisruption>
) {
    var dialogEntity by remember { mutableStateOf<DutchRailwaysDisruption?>(null) }

    dialogEntity?.let {
        AlertDialog(
            onDismissRequest = {
                dialogEntity = null
            },
            title = {
                Text(it.title)
            },
            text = {
                Text(
                    when (it) {
                        is DutchRailwaysDisruption.DisruptionOrMaintenance -> it.description
                        is DutchRailwaysDisruption.Calamity -> it.description ?: ""
                    }
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { dialogEntity = null }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        )
    }

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
                is DutchRailwaysDisruption.DisruptionOrMaintenance -> Disruption(disruption) {
                    dialogEntity = it
                }
                is DutchRailwaysDisruption.Calamity -> Calamity(disruption) {
                    dialogEntity = it
                }
            }

            if (index != disruptions.lastIndex) {
                Divider(Modifier.padding(16.dp, 0.dp))
            } else {
                Spacer(Modifier.height(56.dp))
            }
        }
    }
}
