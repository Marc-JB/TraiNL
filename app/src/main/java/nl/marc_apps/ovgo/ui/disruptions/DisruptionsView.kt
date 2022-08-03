package nl.marc_apps.ovgo.ui.disruptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.ui.DisruptionsList
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
        when {
            disruptions == null -> {
                CircularProgressIndicator()
            }
            disruptions.isEmpty() -> {}
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
