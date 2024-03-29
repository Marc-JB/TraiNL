package nl.marc_apps.ovgo.ui.search_station

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.TrainStationPreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.SubtitleColor

private val SuggestionSpacingHorizontal = 16.dp
private val SuggestionSpacingVertical = 12.dp

private val SuggestionItemSpacing = 8.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StationSuggestions(
    suggestions: List<TrainStation>,
    onSuggestionSelected: (TrainStation) -> Unit
) {
    val listSize = minOf(suggestions.size, integerResource(R.integer.autocomplete_suggestion_list_size))
    LazyColumn {
        items(listSize, key = { index -> suggestions[index].uicCode }) { index ->
            val station = suggestions[index]
            Column(
                Modifier
                    .clickable {
                        onSuggestionSelected(station)
                    }
                    .padding(SuggestionSpacingHorizontal, SuggestionSpacingVertical)
                    .fillMaxWidth()
                    .animateItemPlacement()
            ) {
                Text(station.fullName, style = MaterialTheme.typography.subtitle1, color = SubtitleColor)

                if (station.alternativeNames.isNotEmpty()) {
                    Spacer(Modifier.height(SuggestionItemSpacing))

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            station.alternativeNames.joinToString(),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }

            if (index != listSize - 1) {
                Divider(
                    modifier = Modifier.padding(SuggestionSpacingHorizontal, 0.dp)
                )
            }
        }
    }
}

@Composable
@DayNightPreview
fun StationSuggestionPreview(
    @PreviewParameter(TrainStationPreviewParameterProvider::class) trainStation: TrainStation
) {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            StationSuggestions(listOf(trainStation)) {}
        }
    }
}
