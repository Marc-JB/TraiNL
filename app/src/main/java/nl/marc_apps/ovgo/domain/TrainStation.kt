package nl.marc_apps.ovgo.domain

import androidx.annotation.Keep

@Keep
data class TrainStation(
    val uicCode: String,
    val fullName: String,
    val shortenedName: String,
    val alternativeNames: Set<String> = emptySet(),
    val alternativeSearches: Set<String> = emptySet(),
    val hasDepartureTimesBoard: Boolean = false,
    val hasTravelAssistance: Boolean = false,
    val country: Country? = null
) {
    enum class Country(val flag: String) {
        AUSTRIA("\uD83C\uDDE6\uD83C\uDDF9"),
        BELGIUM("\uD83C\uDDE7\uD83C\uDDEA"),
        SWITZERLAND("\uD83C\uDDE8\uD83C\uDDED"),
        GERMANY("\uD83C\uDDE9\uD83C\uDDEA"),
        FRANCE("\uD83C\uDDEB\uD83C\uDDF7"),
        GREAT_BRITAIN("\uD83C\uDDEC\uD83C\uDDE7"),
        THE_NETHERLANDS("\uD83C\uDDF3\uD83C\uDDF1")
    }
}
