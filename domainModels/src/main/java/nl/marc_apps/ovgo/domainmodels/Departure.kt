package nl.marc_apps.ovgo.domainmodels

import java.text.DateFormat
import java.util.*

data class Departure(
    val direction: String,
    val departureTime: Date,
    val delay: Int,
    val actualDepartureTime: Date,
    val platform: String,
    val platformChanged: Boolean,
    val plannedPlatform: String,
    val journeyNumber: Int,
    val operator: String,
    val category: String,
    val cancelled: Boolean,
    val trainComposition: TrainComposition,
    val majorStops: Array<TrainStation>,
    val messages: Array<Message>
){
    val isDelayed
        get() = Math.round(delay / 60.0) > 0

    val departureTimeText
        get() = DateFormat.getTimeInstance(DateFormat.SHORT).format(departureTime)

    val delayText
        get() = "+${Math.round(delay / 60.0)}"

    val routeStationsText
        get() = if(majorStops.isNotEmpty()) "Via " + majorStops.joinToString { it.name } else ""
}