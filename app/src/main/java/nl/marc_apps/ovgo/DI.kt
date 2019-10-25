package nl.marc_apps.ovgo

import nl.marc_apps.ovgo.api.OVgoApiRepository
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository

object DI {
    val dataRepository: PublicTransportDataRepository by lazy {
        OVgoApiRepository()
    }
}