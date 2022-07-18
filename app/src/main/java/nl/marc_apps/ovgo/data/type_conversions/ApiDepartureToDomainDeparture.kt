package nl.marc_apps.ovgo.data.type_conversions

import android.content.Context
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiDepartureToDomainDeparture(
    private val stations: List<TrainStation>,
    private val trainStation: TrainStation,
    private val resolveTrainInfo: (String) -> TrainInfo?
): KoinComponent {
    private val applicationContext by inject<Context>()

    private fun resolveUicCode(uicCode: String) = stations.find { it.uicCode == uicCode  }

    private fun resolveStationName(stationName: String): TrainStation? {
        return stations.find {
            doesStationNameMatch(it, stationName)
        }
    }

    private fun doesStationNameMatch(trainStation: TrainStation, query: String): Boolean {
        return trainStation.fullName.equals(query, ignoreCase = true)
                || trainStation.shortenedName.equals(query, ignoreCase = true)
                || query in trainStation.alternativeNames
                || query in trainStation.alternativeSearches
    }

    private fun getOperator(model: DutchRailwaysDeparture): String {
        val isRnet = model.product.operatorName.equals(OPERATOR_RNET, ignoreCase = true)
        val isSprinterTrain = model.product.longCategoryName.equals(TRAIN_CATEGORY_SPRINTER, ignoreCase = true)
        return when {
            model.product.longCategoryName == TRAIN_SERVICE_THALYS -> TRAIN_SERVICE_THALYS
            model.product.longCategoryName == TRAIN_SERVICE_EUROSTAR -> TRAIN_SERVICE_EUROSTAR
            !isRnet -> model.product.operatorName
            isSprinterTrain -> applicationContext.getString(R.string.operator_rnet_by_ns)
            else -> applicationContext.getString(R.string.operator_rnet_by_qbuzz)
        }
    }

    private fun getDirectionStation(model: DutchRailwaysDeparture): DirectionStation {
        val endsAtStation = model.messages.firstNotNullOfOrNull {
            when {
                it.message.startsWith(ENDS_AT_TEXT_ENGLISH, ignoreCase = true) -> {
                    it.message.replace(ENDS_AT_TEXT_ENGLISH, "")
                }
                it.message.startsWith(ENDS_AT_TEXT_DUTCH, ignoreCase = true) -> {
                    it.message
                        .replace(ENDS_AT_TEXT_DUTCH, "")
                        .substringBefore(ENDS_AT_TEXT_REASON_PHRASE_DUTCH)
                }
                else -> null
            }
        }

        val endsAtStationResolved = endsAtStation?.let(::resolveStationName)

        return DirectionStation(
            actual = endsAtStationResolved
                ?: model.direction?.let(::resolveStationName)
                ?: model.routeStations.lastOrNull()?.uicCode?.let(::resolveUicCode)
                ?: model.routeStations.lastOrNull()?.mediumName?.let(::resolveStationName),
            planned = if (endsAtStationResolved != null) model.direction?.let(::resolveStationName) else null
        )
    }

    private fun getMessages(model: DutchRailwaysDeparture, level: DutchRailwaysDeparture.DepartureMessage.MessageStyle): Set<String> {
        return model.messages.filter {
            it.style == level
        }.map {
            it.message
        }.toSet()
    }

    fun convert(model: DutchRailwaysDeparture): Departure {
        val (actualDirectionStation, plannedDirectionStation) = getDirectionStation(model)
        val resolvedRouteStations = model.routeStations.mapNotNull {
            resolveUicCode(it.uicCode) ?: resolveStationName(it.mediumName)
        }

        val isForeignService = actualDirectionStation?.country != TrainStation.Country.THE_NETHERLANDS &&
                trainStation.country != TrainStation.Country.THE_NETHERLANDS

        return Departure(
            model.product.number,
            actualDirectionStation ?: model.direction?.let { TrainStation(it, it, it) },
            plannedDirectionStation,
            model.plannedDateTime.toJavaInstant(),
            model.actualDateTime.toJavaInstant(),
            model.plannedTrack,
            model.actualTrack,
            if (isForeignService) null else resolveTrainInfo(model.product.number),
            getOperator(model),
            model.product.longCategoryName,
            model.cancelled,
            resolvedRouteStations,
            getMessages(model, DutchRailwaysDeparture.DepartureMessage.MessageStyle.INFO),
            getMessages(model, DutchRailwaysDeparture.DepartureMessage.MessageStyle.WARNING),
            isForeignService
        )
    }

    private data class DirectionStation(val actual: TrainStation? = null, val planned: TrainStation? = null)

    companion object {
        private const val TRAIN_SERVICE_THALYS = "Thalys"

        private const val TRAIN_SERVICE_EUROSTAR = "Eurostar"

        private const val OPERATOR_RNET = "R-net"

        private const val TRAIN_CATEGORY_SPRINTER = "Sprinter"

        private const val ENDS_AT_TEXT_ENGLISH = "Ends at "

        private const val ENDS_AT_TEXT_DUTCH = "Rijdt niet verder dan "

        private const val ENDS_AT_TEXT_REASON_PHRASE_DUTCH = " door "
    }
}
