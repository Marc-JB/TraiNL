package nl.marc_apps.ovgo.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.domain.TrainStation

@Entity(tableName = "trainstation")
data class TrainStationEntity(
    @PrimaryKey
    val uicCode: String,
    val fullName: String,
    val shortenedName: String,
    val alternativeNames: String,
    val alternativeSearches: String,
    val hasDepartureTimesBoard: Boolean,
    val hasTravelAssistance: Boolean,
    val country: TrainStation.Country
) {
    fun asTrainStation(): TrainStation {
        return TrainStation(
            uicCode,
            fullName,
            shortenedName,
            Json.decodeFromString(alternativeNames),
            Json.decodeFromString(alternativeSearches),
            hasDepartureTimesBoard,
            hasTravelAssistance,
            country
        )
    }

    companion object {
        fun fromTrainStation(trainStation: TrainStation): TrainStationEntity {
            return TrainStationEntity(
                trainStation.uicCode,
                trainStation.fullName,
                trainStation.shortenedName,
                Json.encodeToString(trainStation.alternativeNames),
                Json.encodeToString(trainStation.alternativeSearches),
                trainStation.hasDepartureTimesBoard,
                trainStation.hasTravelAssistance,
                trainStation.country
            )
        }
    }
}
