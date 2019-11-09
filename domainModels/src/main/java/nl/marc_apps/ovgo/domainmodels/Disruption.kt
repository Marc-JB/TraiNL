package nl.marc_apps.ovgo.domainmodels

import java.text.DateFormat
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
    val warningLevel
        get() = when(type) {
            "warning" -> 2
            "disruption" -> 1
            else -> 0
        }

    private val dateTimeFormat
        get() = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)

    val startDateText
        get() = if(startDate == null) null else dateTimeFormat.format(startDate)

    val endDateText
        get() = if(endDate == null) null else dateTimeFormat.format(endDate)
}