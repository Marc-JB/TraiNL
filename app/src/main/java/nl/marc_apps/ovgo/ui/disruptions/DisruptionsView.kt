package nl.marc_apps.ovgo.ui.disruptions

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.ui.components.DisruptionsList
import nl.marc_apps.ovgo.ui.components.PlaceholderImage
import org.koin.androidx.compose.getViewModel

@Composable
fun DisruptionsView(
    disruptionsViewModel: DisruptionsViewModel = getViewModel()
) {
    val disruptionsState by disruptionsViewModel.disruptions.collectAsState()
    val disruptions = disruptionsState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(32.dp))

        Row {
            Icon (Icons.Rounded.Error, contentDescription = null)

            Spacer(Modifier.width(8.dp))

            Text(
                stringResource(R.string.disruptions),
                style = MaterialTheme.typography.h6
            )
        }

        Spacer(Modifier.height(16.dp))

        when {
            disruptions == null -> CircularProgressIndicator()
            disruptions.isEmpty() -> PlaceholderImage(
                text = stringResource(R.string.no_disruptions),
                imageId = R.drawable.va_travelling
            )
            else -> DisruptionsList(disruptions.sortedByDescending {
                when((it as? DutchRailwaysDisruption.Calamity)?.priority) {
                    DutchRailwaysDisruption.Calamity.Priority.PRIO_1 -> 3
                    DutchRailwaysDisruption.Calamity.Priority.PRIO_2 -> 2
                    DutchRailwaysDisruption.Calamity.Priority.PRIO_3 -> 1
                    null -> 0
                }
            })
        }
    }

    LaunchedEffect("") {
        disruptionsViewModel.loadDisruptions()
    }
}
