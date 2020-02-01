package nl.marc_apps.ovgo.domain.models

import java.text.DateFormat.*
import java.util.*

data class Disruption (
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val additionalTravelTime: String?,
    val cause: String?,
    val effect: String?,
    val expectations: String?,
    val startDate: Date?,
    val endDate: Date?
){
    val warningLevel  = when(type) {
        "warning" -> 2
        "disruption" -> 1
        else -> 0
    }

    private val dateTimeFormat = getDateTimeInstance(MEDIUM, SHORT)

    val startDateText = startDate?.format(dateTimeFormat)

    val endDateText = endDate?.format(dateTimeFormat)
}