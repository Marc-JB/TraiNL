package nl.marc_apps.ovgo.ui.search_station

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.theme.AppTheme

@Composable
fun SearchStationView(searchStationViewModel: SearchStationViewModel, navController: NavController) {
    val isLoading by searchStationViewModel.isLoadingData.collectAsState()
    val results by searchStationViewModel.stationSuggestions.collectAsState()

    SearchStationView(
        isLoading,
        results,
        {
            searchStationViewModel.updateAutocompleteList(it)
        },
        {
            val action = SearchStationFragmentDirections.actionStationSearchToHome(it)
            navController.navigate(action)
        }
    )
}

@Composable
fun SearchStationView(
    isLoading: Boolean,
    suggestions: List<TrainStation>,
    updateSuggestions: (String) -> Unit,
    onSuggestionSelected: (TrainStation) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

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

        when {
            isLoading -> CircularProgressIndicator()
            suggestions.isEmpty() -> Text(stringResource(R.string.no_search_results), modifier = Modifier.fillMaxSize())
            else -> StationSuggestions(suggestions, onSuggestionSelected)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchStationViewPreview() {
    val exampleStations = listOf(
        TrainStation("ddr", "Dordrecht", "Dordrecht"),
        TrainStation("rtd", "Rotterdam Centraal", "Rotterdam", setOf("Rotterdam CS", "Rotterdam"))
    )
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            SearchStationView(false, exampleStations, {}, {})
        }
    }
}

@Composable
@Preview
fun SearchStationViewLoadingPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            SearchStationView(true, listOf(), {}, {})
        }
    }
}
