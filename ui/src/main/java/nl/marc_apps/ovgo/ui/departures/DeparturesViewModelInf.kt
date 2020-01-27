package nl.marc_apps.ovgo.ui.departures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nl.marc_apps.ovgo.domain.models.Departure

interface DeparturesViewModelInf {
    val departures: LiveData<Array<Departure>>

    val isLoading: LiveData<Boolean>

    var languageCode: String

    val station: MutableLiveData<String>
}