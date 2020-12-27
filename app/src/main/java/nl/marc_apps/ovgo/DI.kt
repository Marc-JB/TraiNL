package nl.marc_apps.ovgo

import nl.marc_apps.ovgo.api.NsApiRepository
import nl.marc_apps.ovgo.api.OVgoApiRepository
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepositoryV2
import nl.marc_apps.ovgo.domain.services.UserPreferences
import nl.marc_apps.ovgo.ui.MainViewModel
import nl.marc_apps.ovgo.ui.UserPreferencesProvider
import nl.marc_apps.ovgo.ui.departures.DeparturesViewModel
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PublicTransportDataRepository> { OVgoApiRepository() }

    single<PublicTransportDataRepositoryV2> { NsApiRepository(BuildConfig.NSR_KEYS_TRAVEL_API) }

    single<UserPreferences> {
        UserPreferencesProvider(get())
    }

    viewModel {
        DeparturesViewModel(get(), get())
    }

    viewModel {
        DisruptionsViewModel(get(), get())
    }

    viewModel {
        MainViewModel(get())
    }
}