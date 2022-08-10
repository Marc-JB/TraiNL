package nl.marc_apps.ovgo.ui.maintenance

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.ui.components.DisruptionsList
import nl.marc_apps.ovgo.ui.components.PlaceholderImage
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
        Spacer(Modifier.height(32.dp))

        Row {
            Icon (
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                stringResource(R.string.maintenance),
                style = MaterialTheme.typography.h6
            )
        }

        Spacer(Modifier.height(16.dp))

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
