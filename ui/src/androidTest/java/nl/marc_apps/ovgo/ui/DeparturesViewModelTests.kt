package nl.marc_apps.ovgo.ui

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.*
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.ui.departures.DeparturesViewModel
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeparturesViewModelTests {
    @Test
    fun loadStationUpdatesIsLoadingProperly(){
        // Arrange
        val dataRepository = object : PublicTransportDataRepository {
            override var language = "en"

            override suspend fun getDepartures(station: String): Set<Departure> {
                delay(50)
                return emptySet()
            }

            override suspend fun getDisruptions(actual: Boolean) = emptySet<Disruption>()

        }

        val context = InstrumentationRegistry.getInstrumentation().context
        val viewModel = DeparturesViewModel(dataRepository, UserPreferencesProvider(context))

        var isLoadingBefore: Boolean? = null
        var isLoadingAfter: Boolean? = null

        // Act
        runBlocking {
            launch {
                viewModel.loadDepartures()
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