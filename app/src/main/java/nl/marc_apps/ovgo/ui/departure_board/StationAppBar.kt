package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.components.PlatformView
import nl.marc_apps.ovgo.ui.preview.fixtures.TrainStationPreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.BluePrimary

private val AppBarHorizontalSpacing = 16.dp
private val AppBarVerticalSpacing = 8.dp

private val AppBarHeight = 56.dp

private val StationAppBarWhiteSquareSize = 16.dp

private val AppBarIconPadding = 16.dp

@Composable
fun StationAppBar(
    station: TrainStation?,
    onEditStationSelected: () -> Unit
) {
    PlatformView(
        modifier = Modifier
            .padding(AppBarHorizontalSpacing, AppBarVerticalSpacing)
            .fillMaxWidth()
            .height(AppBarHeight),
        whiteSquareSize = StationAppBarWhiteSquareSize,
        backgroundColor = BluePrimary,
        contentColor = Color.White
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))

            if (station != null) {
                Text(
                    station.fullName,
                    style = MaterialTheme.typography.h6
                )
            }

            Box(Modifier.weight(1f)) {
                IconButton(
                    onClick = { onEditStationSelected() },
                    modifier = Modifier
                        .size(AppBarHeight)
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = stringResource(R.string.action_change_station),
                        modifier = Modifier.padding(AppBarIconPadding)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StationAppBarPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            StationAppBar(station = TrainStationPreviewParameterProvider().values.first()) {}
        }
    }
}
