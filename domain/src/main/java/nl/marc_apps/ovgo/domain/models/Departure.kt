package nl.marc_apps.ovgo.domain.models

import java.text.DateFormat
import java.util.*
import kotlin.math.roundToInt

data class Departure(
    val id: Short,
    val direction: TrainStation,
    val actualDepartureTime: Date,
    val plannedDepartureTime: Date,
    val delayInSeconds: Short,
    val actualPlatform: String,
    val plannedPlatform: String,
    val platformChanged: Boolean,
    val operator: String,
    val category: String,
    val cancelled: Boolean,
    val majorStops: Array<TrainStation>,
    val warnings: Array<String>,
    val info: Array<String>,
    val departureStatus: String,
    val trainComposition: TrainComposition
){
    val isDelayed
        get() = delayInSeconds >= 30

    val departureTimeText
        get() = DateFormat.getTimeInstance(DateFormat.SHORT).format(plannedDepartureTime)

    val delayText
        get() = "+${(delayInSeconds / 60.0).roundToInt()}"

    val routeStationsText
        get() = if(majorStops.isNotEmpty()) "Via " + majorStops.joinToString { it.toString() } else ""
}