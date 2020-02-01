package nl.marc_apps.ovgo.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.ui.departures.DeparturesViewModel
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeparturesViewModelTests {
    @Test
    fun loadStationUpdatesIsLoadingProperly(){
        // Arrange
        val dataRepository = object : PublicTransportDataRepository {
            override var language = "en"

            override suspend fun getDepartures(station: String): Array<Departure> {
                delay(75)
                return emptyArray()
            }

            override suspend fun getDisruptions(actual: Boolean) = emptyArray<Disruption>()

        }

        val viewModel = DeparturesViewModel(dataRepository)

        var isLoadingBefore: Boolean? = null
        var isLoadingAfter: Boolean? = null

        // Act
        runBlocking {
            launch {
                viewModel.loadStations("Test")
            }

            delay(25)
            isLoadingBefore = viewModel.isLoading.value
            delay(75)
            isLoadingAfter = viewModel.isLoading.value
        }

        // Assert
        assertTrue(isLoadingBefore ?: false)
        assertFalse(isLoadingAfter ?: true)
    }
}