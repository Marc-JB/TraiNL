package nl.marc_apps.ovgo.domain.services

import nl.marc_apps.ovgo.domain.models.TrainStation

interface PublicTransportDataRepositoryV2 {
    suspend fun getAllStations(): List<TrainStation>
}
