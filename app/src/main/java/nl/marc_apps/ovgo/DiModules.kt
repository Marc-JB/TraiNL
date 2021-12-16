package nl.marc_apps.ovgo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.ImageLoader
import kotlinx.serialization.ExperimentalSerializationApi
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.TrainInfoRepository
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.HttpClient
import nl.marc_apps.ovgo.data.api.HttpClientImpl
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.db.AppDatabase
import nl.marc_apps.ovgo.domain.DeviceConfiguration
import nl.marc_apps.ovgo.search.JaroWinklerStringSimilarity
import nl.marc_apps.ovgo.search.StringSimilarity
import nl.marc_apps.ovgo.ui.departure_board.DepartureBoardViewModel
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsViewModel
import nl.marc_apps.ovgo.ui.maintenance.MaintenanceViewModel
import nl.marc_apps.ovgo.ui.search_station.SearchStationViewModel
import nl.marc_apps.ovgo.utils.buildRoomDatabase
import nl.marc_apps.ovgo.utils.isLowRamDevice
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalSerializationApi
object DiModules {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val dataRepositoriesModule = module {
        single {
            get<Context>().dataStore
        }

        single {
            DeviceConfiguration(
                get<Context>().isLowRamDevice ?: false
            )
        }

        single {
            DutchRailwaysApi(
                httpClient = get(),
                BuildConfig.DUTCH_RAILWAYS_TRAVEL_INFO_API_KEY,
                get<Context>().getString(R.string.dutch_railways_api_language_code)
            )
        }

        single {
            buildRoomDatabase<AppDatabase>(get(), "app-database")
        }

        single {
            get<AppDatabase>().trainStationDao()
        }

        single { TrainStationRepository(get(), get(), get()) }
        single { TrainInfoRepository(get(), get()) }
        single { DepartureRepository(get(), get(), get()) }
    }

    val viewModelsModule = module {
        viewModel { DepartureBoardViewModel(get(), get(), get()) }
        viewModel { DisruptionsViewModel(get()) }
        viewModel { MaintenanceViewModel(get()) }
        viewModel { SearchStationViewModel(get(), get()) }
    }

    val utilitiesModule = module {
        single<HttpClient> { HttpClientImpl(get()) }
        single<StringSimilarity> { JaroWinklerStringSimilarity }
        single {
            ImageLoader.Builder(get())
                .okHttpClient(get<HttpClient>().okHttpClient)
                .build()
        }
    }
}
