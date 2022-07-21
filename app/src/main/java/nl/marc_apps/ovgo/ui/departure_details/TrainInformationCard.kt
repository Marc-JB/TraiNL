package nl.marc_apps.ovgo.ui.departure_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.ui.TrainImagesView
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.Card

@Composable
fun TrainInformationCard(departure: Departure, imageLoader: ImageLoader? = null) {
    Card(applyHorizontalPadding = false) { padding: Dp ->
        Text(
            stringResource(R.string.traintype_and_facilities),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(padding, 0.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val facilities = departure.trainInfo?.facilities ?: TrainInfo.TrainFacilities()
        FacilityView(facilities, Modifier.padding(padding, 0.dp))

        Spacer(modifier = Modifier.height(8.dp))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                stringResource(
                    R.string.departure_operator_and_type_single_line,
                    departure.operator,
                    departure.categoryName
                ),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(padding, 0.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (departure.trainInfo?.trainParts?.firstOrNull()?.imageUrl != null) {
            val imageUrls = departure.trainInfo.trainParts.mapNotNull { it.imageUrl }
            TrainImagesView(imageUrls, padding, padding, imageLoader)
        }
    }
}

@Composable
fun FacilityView(facilities: TrainInfo.TrainFacilities, modifier: Modifier = Modifier) {
    Row(modifier) {
        FacilityIcon(
            enabled = facilities.isWheelChairAccessible,
            iconPainter = painterResource(R.drawable.ic_accessible),
            description = "Wheelchair accessible"
        )

        FacilityIcon(
            enabled = facilities.hasToilet,
            iconPainter = painterResource(R.drawable.ic_toilet),
            description = "Toilet"
        )

        FacilityIcon(
            enabled = facilities.hasBicycleCompartment,
            iconPainter = painterResource(R.drawable.ic_bike),
            description = "Bicycles"
        )

        FacilityIcon(
            enabled = facilities.hasFreeWifi,
            iconPainter = painterResource(R.drawable.ic_wifi),
            description = "Wifi"
        )

        FacilityIcon(
            enabled = facilities.hasPowerSockets,
            iconPainter = painterResource(R.drawable.ic_power),
            description = "Power sockets"
        )

        FacilityIcon(
            enabled = facilities.hasSilenceCompartment,
            iconPainter = painterResource(R.drawable.ic_silence),
            description = "Silence compartment"
        )

        FacilityIcon(
            enabled = facilities.hasFirstClass,
            iconPainter = painterResource(R.drawable.ic_first_class),
            description = "First class"
        )

        FacilityIcon(
            enabled = facilities.hasBistro,
            iconPainter = painterResource(R.drawable.ic_bistro),
            description = "Bistro"
        )
    }
}

@Composable
fun FacilityIcon(enabled: Boolean, iconPainter: Painter, description: String) {
    if (enabled) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.primary) {
            Icon(iconPainter, contentDescription = description)
        }
    } else {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Icon(iconPainter, contentDescription = description)
        }
    }

    Spacer(Modifier.width(4.dp))
}

@Preview
@Composable
fun FacilityViewPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            FacilityView(
                TrainInfo.TrainFacilities(
                    hasFreeWifi = true,
                    hasToilet = true,
                    isWheelChairAccessible = true,
                    hasBicycleCompartment = true,
                    hasBistro = false,
                    hasPowerSockets = true,
                    hasSilenceCompartment = false,
                    seatsFirstClass = 0,
                    seatsSecondClass = 300
                ),
                Modifier.padding(16.dp)
            )
        }
    }
}
