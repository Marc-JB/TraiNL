package nl.marc_apps.ovgo.data.type_conversions

import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo.TrainPart.Facility as DutchRailwaysFacility

object TrainInfoConversions {
    private const val TRAIN_IMAGE_URL_QBUZZ_GTW_6 = "https://marc-jb.github.io/TraiNL-resources/gtw_qbuzz_26.png"

    private const val TRAIN_IMAGE_URL_QBUZZ_GTW_8 = "https://marc-jb.github.io/TraiNL-resources/gtw_qbuzz_28.png"

    private const val TRAIN_IMAGE_URL_EUROSTAR = "https://marc-jb.github.io/TraiNL-resources/eurostar_e320.png"

    private const val TRAIN_SEAT_COUNT_QBUZZ_GTW_6 = 113

    private const val TRAIN_SEAT_COUNT_QBUZZ_GTW_8 = 172

    private val JOURNEY_NUMBERS_RANGE_QBUZZ = 7100..7299

    private const val NO_SEATS = 0

    private const val OPERATOR_NAME_RNET = "R-net"

    private const val OPERATOR_NAME_EUROSTAR = "Eurostar"

    private const val TRAIN_TYPE_RNET_BY_NS = "Flirt 2 TAG"

    private const val TRAIN_TYPE_EUROSTAR = "Eurostar"

    private fun isQbuzzDMG(model: DutchRailwaysTrainInfo): Boolean {
        return model.journeyNumber in JOURNEY_NUMBERS_RANGE_QBUZZ || (
                model.operator.equals(OPERATOR_NAME_RNET, ignoreCase = true) &&
                        model.actualTrainParts.none {
                            it.type.equals(TRAIN_TYPE_RNET_BY_NS, ignoreCase = true)
                        }
                )
    }

    fun convertApiToDomainModel(
        model: DutchRailwaysTrainInfo,
        departure: DutchRailwaysDeparture? = null
    ): TrainInfo {
        val isQbuzzDMG = isQbuzzDMG(model)

        return TrainInfo(
            model.journeyNumber,
            model.actualTrainParts.map {
                convertApiTrainPartToDomain(
                    it,
                    isQbuzzDMG,
                    departure?.product?.longCategoryName.equals(OPERATOR_NAME_EUROSTAR, ignoreCase = true) ||
                            model.operator.equals(OPERATOR_NAME_EUROSTAR, ignoreCase = true)
                )
            }
        )
    }

    private fun convertApiTrainPartToDomain(
        model: DutchRailwaysTrainInfo.TrainPart,
        isQbuzzDMG: Boolean,
        isEurostar: Boolean
    ): TrainInfo.TrainPart {
        return if (isQbuzzDMG) {
            convertQbuzzApiTrainPartToDomain(model)
        } else {
            convertRegularApiTrainPartToDomain(model, isEurostar)
        }
    }

    private fun convertQbuzzApiTrainPartToDomain(
        model: DutchRailwaysTrainInfo.TrainPart
    ): TrainInfo.TrainPart {
        val isLongTrainPart = "8" in model.type
        return TrainInfo.TrainPart(
            model.id,
            TrainInfo.TrainFacilities(
                seatsFirstClass = NO_SEATS,
                seatsSecondClass = if (isLongTrainPart) {
                    TRAIN_SEAT_COUNT_QBUZZ_GTW_8
                } else {
                    TRAIN_SEAT_COUNT_QBUZZ_GTW_6
                },
                hasToilet = false,
                hasSilenceCompartment = false,
                hasPowerSockets = true,
                isWheelChairAccessible = true,
                hasBicycleCompartment = true,
                hasFreeWifi = true,
                hasBistro = false,
            ),
            if (isLongTrainPart) {
                TRAIN_IMAGE_URL_QBUZZ_GTW_8
            } else {
                TRAIN_IMAGE_URL_QBUZZ_GTW_6
            }
        )
    }

    private fun convertRegularApiTrainPartToDomain(
        model: DutchRailwaysTrainInfo.TrainPart,
        isEurostar: Boolean
    ): TrainInfo.TrainPart {
        val seatCountFirstClass = (model.seats?.foldingChairsFirstClass ?: NO_SEATS) +
                (model.seats?.seatsFirstClass ?: NO_SEATS)
        val seatCountSecondClass = (model.seats?.foldingChairsSecondClass ?: NO_SEATS) +
                (model.seats?.seatsSecondClass ?: NO_SEATS)

        return TrainInfo.TrainPart(
            model.id,
            TrainInfo.TrainFacilities(
                seatCountFirstClass,
                seatCountSecondClass,
                DutchRailwaysFacility.TOILET in model.facilities,
                DutchRailwaysFacility.SILENCE_COMPARTMENT in model.facilities,
                DutchRailwaysFacility.POWER_SOCKETS in model.facilities,
                DutchRailwaysFacility.WHEELCHAIR_ACCESSIBLE in model.facilities,
                DutchRailwaysFacility.BICYCLE_COMPARTMENT in model.facilities,
                DutchRailwaysFacility.WIFI in model.facilities,
                DutchRailwaysFacility.BISTRO in model.facilities,
            ),
            if (isEurostar || model.type.equals(TRAIN_TYPE_EUROSTAR, ignoreCase = true)) {
                TRAIN_IMAGE_URL_EUROSTAR
            } else {
                model.imageUrl
            }
        )
    }
}
