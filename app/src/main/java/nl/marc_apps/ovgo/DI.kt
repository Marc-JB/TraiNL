package nl.marc_apps.ovgo

import nl.marc_apps.ovgo.api.NsApiRepository
import nl.marc_apps.ovgo.api.OVgoApiRepository
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepositoryV2
import nl.marc_apps.ovgo.ui.MainViewModel
import nl.marc_apps.ovgo.ui.departures.DeparturesViewModel
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PublicTransportDataRepository> { OVgoApiRepository() }

    single<PublicTransportDataRepositoryV2> { NsApiRepository(BuildConfig.NSR_KEYS_TRAVEL_API) }

    viewModel {
        DeparturesViewModel(get())
    }

    viewModel {
        DisruptionsViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }
}