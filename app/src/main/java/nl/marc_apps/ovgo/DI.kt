package nl.marc_apps.ovgo

import nl.marc_apps.ovgo.api.OVgoApiRepository
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.ui.MainViewModel
import nl.marc_apps.ovgo.ui.departures.DeparturesViewModel
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsViewModel
import nl.marc_apps.ovgo.ui.trips.TripsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PublicTransportDataRepository> { OVgoApiRepository() }

    viewModel {
        DeparturesViewModel(get())
    }

    viewModel {
        DisruptionsViewModel(get())
    }

    viewModel {
        TripsViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }
}