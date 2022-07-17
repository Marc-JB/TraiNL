package nl.marc_apps.ovgo.ui.search_station

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.composethemeadapter.MdcTheme
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation

@Composable
fun StationSuggestions(suggestions: List<TrainStation>, onSuggestionSelected: (TrainStation) -> Unit) {
    val listSize = minOf(suggestions.size, integerResource(R.integer.autocomplete_suggestion_list_size))
    LazyColumn {
        items(listSize, key = { index -> suggestions[index].uicCode }) { index ->
            val station = suggestions[index]
            Column(
                Modifier
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

@Composable
@Preview
fun StationSuggestionPreview() {
    val exampleStation = TrainStation("rtd", "Rotterdam Centraal", "Rotterdam", setOf("Rotterdam CS", "Rotterdam"))
    MdcTheme {
        Surface(color = MaterialTheme.colors.background) {
            StationSuggestions(suggestions = listOf(exampleStation), onSuggestionSelected = {})
        }
    }
}
