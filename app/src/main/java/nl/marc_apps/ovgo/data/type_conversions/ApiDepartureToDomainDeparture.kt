package nl.marc_apps.ovgo.data.type_conversions

import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation

class ApiDepartureToDomainDeparture(
    private val stations: List<TrainStation>,
    private val resolveTrainInfo: (String) -> TrainInfo?
) {
    private fun resolveUicCode(uicCode: String) = stations.find { it.uicCode == uicCode  }

    private fun resolveStationName(stationName: String): TrainStation? {
        return stations.find {
            it.name == stationName
                    || stationName in it.alternativeNames
                    || stationName in it.alternativeSearches
        }
    }

    private fun getOperator(model: DutchRailwaysDeparture) = when {
        model.product.longCategoryName == TRAIN_SERVICE_THALYS -> TRAIN_SERVICE_THALYS
        model.product.longCategoryName == TRAIN_SERVICE_EUROSTAR -> TRAIN_SERVICE_EUROSTAR
        !model.product.operatorName.equals(OPERATOR_RNET, ignoreCase = true) -> model.product.operatorName
        model.product.longCategoryName.equals(TRAIN_CATEGORY_SPRINTER, ignoreCase = true) -> OPERATOR_RNET_BY_NS
        else -> OPERATOR_RNET_BY_QBUZZ
    }

    private fun getDirectionStation(model: DutchRailwaysDeparture): TrainStation? {
        return model.direction?.let(::resolveStationName)
            ?: model.routeStations.lastOrNull()?.uicCode?.let(::resolveUicCode)
            ?: model.routeStations.lastOrNull()?.mediumName?.let(::resolveStationName)
    }

    fun convert(model: DutchRailwaysDeparture): Departure {
        return Departure(
            model.product.number,
            getDirectionStation(model),
            model.plannedDateTime,
            model.actualDateTime,
            model.plannedTrack,
            model.actualTrack,
            resolveTrainInfo(model.product.number),
            getOperator(model),
            model.product.longCategoryName,
            model.cancelled,
            model.routeStations.mapNotNull {
                resolveUicCode(it.uicCode) ?: resolveStationName(it.mediumName)
            }
        )
    }

    companion object {
        private const val TRAIN_SERVICE_THALYS = "Thalys"

        private const val TRAIN_SERVICE_EUROSTAR = "Eurostar"

        private const val OPERATOR_RNET = "R-net"

        private const val TRAIN_CATEGORY_SPRINTER = "Sprinter"

        // TODO: Migrate this constant to a string resource
        private const val OPERATOR_RNET_BY_QBUZZ = "R-net door Qbuzz"

        // TODO: Migrate this constant to a string resource
        private const val OPERATOR_RNET_BY_NS = "R-net door NS"
    }
}
