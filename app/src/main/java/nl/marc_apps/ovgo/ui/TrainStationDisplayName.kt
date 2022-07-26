package nl.marc_apps.ovgo.ui

import nl.marc_apps.ovgo.domain.TrainStation

object TrainStationDisplayName {
    private const val STATION_NAME_CHARACTER_LIMIT = 18

    private const val STATION_NAME_FLAG_CHARACTER_COUNT = 2

    private fun isForeignStation(trainStation: TrainStation): Boolean {
        return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
    }

    fun createDisplayName(trainStation: TrainStation, characterLimit: Int = STATION_NAME_CHARACTER_LIMIT): String {
        val effectiveCharacterLimit = if (isForeignStation(trainStation)) {
            characterLimit - STATION_NAME_FLAG_CHARACTER_COUNT
        } else {
            characterLimit
        }

        return buildString {
            append(
                if (trainStation.fullName.length > effectiveCharacterLimit) {
                    trainStation.shortenedName
                } else {
                    trainStation.fullName
                }
            )

            if (isForeignStation(trainStation)) {
                append(" ")
                append(trainStation.country?.flag)
            }
        }
    }
}
