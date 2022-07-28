package nl.marc_apps.ovgo.ui.departure_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.preview.fixtures.JourneyStopPreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun DepartureDetailsView(
    departure: Departure,
    departureDetailsViewModel: DepartureDetailsViewModel = getViewModel(),
    navController: NavController,
    imageLoader: ImageLoader = get()
) {
    val stops by departureDetailsViewModel.journeyStops.collectAsState()

    DepartureDetailsView(
        departure,
        stops,
        imageLoader,
        onStationSelected = {
            val action = DepartureDetailsFragmentDirections
                .actionDepartureDetailsToStationDepartureBoard(it)
            navController.navigate(action)
        }
    )

    LaunchedEffect(departure) {
        departureDetailsViewModel.loadStations(departure)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DepartureDetailsView(
    departure: Departure,
    stops: List<JourneyStop>?,
    imageLoader: ImageLoader? = null,
    onStationSelected: (TrainStation) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 56.dp)) {
                stops?.let {
                    DepartureStopsView(it, onStationSelected)
                }
            }
        }
    ) {
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

            RouteInformationCard(
                departure,
                stops,
                onStationSelected = onStationSelected,
                showAllStops = {
                    coroutineScope.launch {
                        modalBottomSheetState.show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrainInformationCard(departure, imageLoader)

            Spacer(modifier = Modifier.height(16.dp))
        }

        BackHandler(enabled = modalBottomSheetState.isVisible) {
            coroutineScope.launch {
                modalBottomSheetState.hide()
            }
        }
    }
}

@DayNightPreview
@Preview
@Composable
fun DepartureDetailsViewPreview(
    @PreviewParameter(DeparturePreviewParameterProvider::class) departure: Departure
) {
    val stops = JourneyStopPreviewParameterProvider().values.toList()
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DepartureDetailsView(departure, stops, null) {}
        }
    }
}
