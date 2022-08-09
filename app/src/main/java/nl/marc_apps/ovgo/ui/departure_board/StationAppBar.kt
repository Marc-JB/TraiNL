package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.preview.fixtures.TrainStationPreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.BluePrimary

private val AppBarHorizontalSpacing = 16.dp
private val AppBarVerticalSpacing = 8.dp

private val AppBarCornerRadius = 2.dp

private val AppBarHeight = 56.dp

@Composable
fun StationAppBar(
    station: TrainStation?,
    onEditStationSelected: () -> Unit
) {
    Box(
        Modifier
            .padding(AppBarHorizontalSpacing, AppBarVerticalSpacing)
            .fillMaxWidth()
            .height(AppBarHeight)
            .shadow(
                dimensionResource(R.dimen.departure_board_action_bar_elevation),
                RoundedCornerShape(AppBarCornerRadius)
            )
            .background(
                BluePrimary,
                RoundedCornerShape(
                    topStart = AppBarCornerRadius + 2.dp,
                    AppBarCornerRadius,
                    AppBarCornerRadius,
                    AppBarCornerRadius
                )
            ),
    ) {
        Box (
            modifier = Modifier
                .size(16.dp)
                .background(Color.White, RoundedCornerShape(topStart = AppBarCornerRadius))
                .align(Alignment.TopStart)
        ) {}

        CompositionLocalProvider(LocalContentColor provides Color.White) {
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
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = stringResource(R.string.action_change_station),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
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
