package nl.marc_apps.ovgo.domain.services

import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.models.Disruption

interface PublicTransportDataRepository {
    var language: String
    suspend fun getDepartures(station: String): Array<Departure>
    suspend fun getDisruptions(actual: Boolean = true): Array<Disruption>
}