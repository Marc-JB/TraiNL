package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.LayoutState
import nl.marc_apps.ovgo.ui.TrainStationDisplayName
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

private val DepartureViewSpacingVertical = 8.dp

@Composable
fun CancelledDepartureView(departure: Departure, layoutState: LayoutState = LayoutState()) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            modifier = Modifier.padding(layoutState.windowPadding.width, DepartureViewSpacingVertical)
        ) {
            Text(
                departure.actualDepartureTime.format(timeStyle = DateFormat.SHORT),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            )

            Spacer(Modifier.width(2.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alignByBaseline()
            ) {
                Text(
                    (departure.actualDirection ?: departure.plannedDirection)?.let {
                        TrainStationDisplayName.createDisplayName(it)
                    } ?: "",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.height(2.dp))

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        stringResource(R.string.departure_cancelled),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.sectionTitleWarningColor)
                    )
                }
            }

            Spacer(Modifier.width(2.dp))

            Text(
                if (departure.operator == departure.categoryName) {
                    departure.operator
                } else {
                    stringResource(
                        R.string.departure_operator_and_type_multi_line,
                        departure.operator,
                        departure.categoryName
                    )
                },
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline(),
                textAlign = TextAlign.End
            )
        }
    }
}

@DayNightPreview
@Composable
fun CancelledDepartureViewPreview(
    @PreviewParameter(DeparturePreviewParameterProvider::class) departure: Departure
) {
    AppTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            CancelledDepartureView(departure.copy(isCancelled = true))
        }
    }
}
