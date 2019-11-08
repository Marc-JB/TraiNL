package nl.marc_apps.ovgo

import nl.marc_apps.ovgo.api.OVgoApiRepository
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import nl.marc_apps.ovgo.viewmodels.DeparturesViewModel
import nl.marc_apps.ovgo.viewmodels.DisruptionsViewModel
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
}