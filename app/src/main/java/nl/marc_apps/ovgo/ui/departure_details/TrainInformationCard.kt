package nl.marc_apps.ovgo.ui.departure_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.ui.components.Card
import nl.marc_apps.ovgo.ui.components.TrainImagesView
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme

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
            vectorIcon = Icons.Rounded.Accessible,
            description = "Wheelchair accessible"
        )

        FacilityIcon(
            enabled = facilities.hasToilet,
            iconPainter = painterResource(R.drawable.ic_toilet),
            description = "Toilet"
        )

        FacilityIcon(
            enabled = facilities.hasBicycleCompartment,
            vectorIcon = Icons.Rounded.DirectionsBike,
            description = "Bicycles"
        )

        FacilityIcon(
            enabled = facilities.hasFreeWifi,
            vectorIcon = Icons.Rounded.Wifi,
            description = "Wifi"
        )

        FacilityIcon(
            enabled = facilities.hasPowerSockets,
            vectorIcon = Icons.Rounded.Power,
            description = "Power sockets"
        )

        FacilityIcon(
            enabled = facilities.hasSilenceCompartment,
            vectorIcon = Icons.Rounded.VoiceOverOff,
            description = "Silence compartment"
        )

        FacilityIcon(
            enabled = facilities.hasFirstClass,
            vectorIcon = Icons.Rounded.LooksOne,
            description = "First class"
        )

        FacilityIcon(
            enabled = facilities.hasBistro,
            vectorIcon = Icons.Rounded.Restaurant,
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

@Composable
fun FacilityIcon(enabled: Boolean, vectorIcon: ImageVector, description: String) {
    if (enabled) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.primary) {
            Icon(vectorIcon, contentDescription = description)
        }
    } else {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Icon(vectorIcon, contentDescription = description)
        }
    }

    Spacer(Modifier.width(4.dp))
}

@DayNightPreview
@Composable
fun TrainInformationCardPreview(
    @PreviewParameter(DeparturePreviewParameterProvider::class) departure: Departure
) {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            TrainInformationCard(departure)
        }
    }
}

@DayNightPreview
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
