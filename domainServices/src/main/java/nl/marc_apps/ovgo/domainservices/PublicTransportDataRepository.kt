package nl.marc_apps.ovgo.domainservices

import nl.marc_apps.ovgo.domainmodels.Departure
import nl.marc_apps.ovgo.domainmodels.Disruption

interface PublicTransportDataRepository {
    var language: String
    suspend fun getDepartures(station: String): Array<Departure>
    suspend fun getDisruptions(actual: Boolean = true): Array<Disruption>
}