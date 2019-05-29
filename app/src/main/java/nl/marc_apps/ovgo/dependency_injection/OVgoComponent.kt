package nl.marc_apps.ovgo.dependency_injection

import dagger.Component
import nl.marc_apps.ovgo.viewmodels.DeparturesViewModel
import nl.marc_apps.ovgo.viewmodels.DisruptionsViewModel

@Component(modules = [OVgoModule::class])
interface OVgoComponent {
    fun inject(viewModel: DeparturesViewModel)
    fun inject(viewModel: DisruptionsViewModel)
}