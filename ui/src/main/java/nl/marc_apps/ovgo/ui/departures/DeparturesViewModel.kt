package nl.marc_apps.ovgo.ui.departures

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class DeparturesViewModel(val dataRepository: PublicTransportDataRepository) : ViewModel(), DeparturesViewModelInf {
    override val departures: MutableLiveData<Array<Departure>> = MutableLiveData(emptyArray())

    override val isLoading = MutableLiveData(true)

    override var languageCode: String = "en"
        set(value){
            field = value
            dataRepository.language = value
        }

    override val station: MutableLiveData<String> = MutableLiveData()
}
