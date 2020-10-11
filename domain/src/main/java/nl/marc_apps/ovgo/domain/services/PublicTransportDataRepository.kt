package nl.marc_apps.ovgo.domain.services

import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.models.Disruption

interface PublicTransportDataRepository {
    var language: String
    suspend fun getDepartures(station: String): Set<Departure>
    suspend fun getDisruptions(actual: Boolean = true): Set<Disruption>
}