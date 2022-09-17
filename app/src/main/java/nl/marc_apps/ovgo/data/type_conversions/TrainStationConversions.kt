package nl.marc_apps.ovgo.data.type_conversions

import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStation
import nl.marc_apps.ovgo.domain.TrainStation

object TrainStationConversions {
    fun convertApiToDomainModel(model: DutchRailwaysStation): TrainStation {
        return TrainStation(
            model.uicCode,
            model.names.long,
            model.names.middle,
            model.customNames,
            setOf(model.dutchRailwaysCode, model.names.short),
            model.hasDepartureTimesBoard,
            model.hasTravelAssistance,
            model.country?.name?.let { TrainStation.Country.valueOf(it) }
        )
    }
}
