package nl.marc_apps.ovgo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.ImageLoader
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.JourneyDetailsRepository
import nl.marc_apps.ovgo.data.TrainInfoRepository
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.HttpClient
import nl.marc_apps.ovgo.data.api.HttpClientImpl
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTrainInfoApi
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysTravelInfoApi
import nl.marc_apps.ovgo.data.db.AppDatabase
import nl.marc_apps.ovgo.search.JaroWinklerStringSimilarity
import nl.marc_apps.ovgo.search.StringSimilarity
import nl.marc_apps.ovgo.ui.departure_board.DepartureBoardViewModel
import nl.marc_apps.ovgo.ui.departure_details.DepartureDetailsViewModel
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsViewModel
import nl.marc_apps.ovgo.ui.maintenance.MaintenanceViewModel
import nl.marc_apps.ovgo.ui.search_station.SearchStationViewModel
import nl.marc_apps.ovgo.utils.buildRoomDatabase
import nl.marc_apps.ovgo.utils.retrofit
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DiModules {
    private const val APP_DATABASE_NAME = "app-database"

    private const val APP_SETTINGS_NAME = "settings"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_SETTINGS_NAME)

    val dataRepositoriesModule = module {
        single {
            get<Context>().dataStore
        }

        single {
            retrofit<DutchRailwaysTravelInfoApi> {
                baseUrl(DutchRailwaysTravelInfoApi.BASE_URL)
                addConverterFactory(get<HttpClient>().jsonConverter)
                client(get<HttpClient>().okHttpClient)
            }
        }

        single {
            retrofit<DutchRailwaysTrainInfoApi> {
                baseUrl(DutchRailwaysTrainInfoApi.BASE_URL)
                addConverterFactory(get<HttpClient>().jsonConverter)
                client(get<HttpClient>().okHttpClient)
            }
        }

        single {
            DutchRailwaysApi(
                travelInfoApi = get(),
                trainInfoApi = get(),
                BuildConfig.DUTCH_RAILWAYS_TRAVEL_INFO_API_KEY,
                get<Context>().getString(R.string.dutch_railways_api_language_code)
            )
        }

        single {
            buildRoomDatabase<AppDatabase>(get(), APP_DATABASE_NAME)
        }

        single {
            get<AppDatabase>().trainStationDao()
        }

        singleOf(::TrainStationRepository)
        singleOf(::TrainInfoRepository)
        singleOf(::DepartureRepository)
        singleOf(::JourneyDetailsRepository)
    }

    val viewModelsModule = module {
        viewModelOf(::DepartureBoardViewModel)
        viewModelOf(::DepartureDetailsViewModel)
        viewModelOf(::DisruptionsViewModel)
        viewModelOf(::MaintenanceViewModel)
        viewModelOf(::SearchStationViewModel)
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
