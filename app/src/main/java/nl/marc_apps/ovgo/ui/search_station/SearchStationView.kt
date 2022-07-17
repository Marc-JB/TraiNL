package nl.marc_apps.ovgo.ui.search_station

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.composethemeadapter.MdcTheme
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation

@Composable
fun SearchStationView(searchStationViewModel: SearchStationViewModel, onSuggestionSelected: (TrainStation) -> Unit) {
    val isLoading = searchStationViewModel.isLoadingData
    val results = searchStationViewModel.stationSuggestions

    SearchStationView(
        isLoading.value,
        results.value,
        {
            searchStationViewModel.updateAutocompleteList(it)
        },
        onSuggestionSelected
    )
}

@Composable
fun SearchStationView(
    isLoading: Boolean,
    suggestions: List<TrainStation>,
    updateSuggestions: (String) -> Unit,
    onSuggestionSelected: (TrainStation) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        updateSuggestions(searchQuery)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val searchQueryHint = stringResource(id = R.string.station)

        OutlinedTextField(
            value = searchQuery, 
            onValueChange = { searchQuery = it },
            label = { Text(searchQueryHint) },
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Icon(Icons.Rounded.Clear, contentDescription = "clear", Modifier.clickable {
                        searchQuery = ""
                    })
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            StationSuggestions(suggestions, onSuggestionSelected)
        }
    }
}

@Composable
fun StationSuggestions(suggestions: List<TrainStation>, onSuggestionSelected: (TrainStation) -> Unit) {
    val listSize = minOf(suggestions.size, integerResource(R.integer.autocomplete_suggestion_list_size))
    LazyColumn {
        items(listSize) { index ->
            val station = suggestions[index]
            Column(Modifier
                .clickable {
                    onSuggestionSelected(station)
                }
                .padding(16.dp, 12.dp)
                .fillMaxWidth()
            ) {
                Text(station.fullName, color = colorResource(id = R.color.sectionTitleColor), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                if (station.alternativeNames.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(station.alternativeNames.joinToString(), fontSize = 14.sp)
                }
            }

            if (index != listSize - 1) {
                Divider(
                    modifier = Modifier.padding(16.dp, 0.dp)
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchStationViewPreview() {
    val exampleStations = listOf(
        TrainStation("ddr", "Dordrecht", "Dordrecht"),
        TrainStation("rtd", "Rotterdam CS", "Rotterdam", setOf("Rotterdam Centraal"))
    )
    MdcTheme {
        Surface(color = MaterialTheme.colors.background) {
            SearchStationView(false, exampleStations, {}, {})
        }
    }
}

@Composable
@Preview
fun SearchStationViewLoadingPreview() {
    MdcTheme {
        Surface(color = MaterialTheme.colors.background) {
            SearchStationView(true, listOf(), {}, {})
        }
    }
}

@Composable
@Preview
fun StationSuggestionPreview() {
    val exampleStation = TrainStation("ddr", "Dordrecht", "Dordrecht", setOf("Dordt"))
    MdcTheme {
        Surface(color = MaterialTheme.colors.background) {
            StationSuggestions(suggestions = listOf(exampleStation), onSuggestionSelected = {})
        }
    }
}
