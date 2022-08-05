package nl.marc_apps.ovgo.ui.maintenance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.ui.DisruptionsList
import nl.marc_apps.ovgo.ui.PlaceholderImage
import org.koin.androidx.compose.getViewModel

@Composable
fun MaintenanceView(
    maintenanceViewModel: MaintenanceViewModel = getViewModel()
) {
    val maintenanceState by maintenanceViewModel.maintenanceList.collectAsState()
    val maintenanceList = maintenanceState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            maintenanceList == null -> CircularProgressIndicator()
            maintenanceList.isEmpty() -> PlaceholderImage(
                text = stringResource(R.string.no_maintenance),
                imageId = R.drawable.va_travelling
            )
            else -> DisruptionsList(maintenanceList)
        }
    }

    LaunchedEffect("") {
        maintenanceViewModel.loadMaintenance()
    }
}
