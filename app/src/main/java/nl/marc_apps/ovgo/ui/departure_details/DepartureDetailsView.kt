package nl.marc_apps.ovgo.ui.departure_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun DepartureDetailsView(
    departure: Departure,
    departureDetailsViewModel: DepartureDetailsViewModel = getViewModel(),
    navController: NavController,
    imageLoader: ImageLoader = get()
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = departure.actualDirection?.fullName?.let {
                stringResource(R.string.departure_info_title, departure.categoryName, it)
            } ?: departure.categoryName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(24.dp, 0.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DepartureInformationCard(departure)

        Spacer(modifier = Modifier.height(16.dp))

        RouteInformationCard(departure, departureDetailsViewModel, navController)

        Spacer(modifier = Modifier.height(16.dp))

        TrainInformationCard(departure, imageLoader)

        Spacer(modifier = Modifier.height(16.dp))
    }
}
