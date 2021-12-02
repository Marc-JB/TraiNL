package nl.marc_apps.ovgo.utils.serialization

import java.text.SimpleDateFormat
import java.util.*

object JsonDateTime {
    private const val JSON_DATE_AND_TIME_PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ssX"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSSX"

    val defaultParser = SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE, Locale.US)

    val parsers = arrayOf(
        defaultParser,
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE, Locale.US),
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC, Locale.US),
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL, Locale.US)
    )
}
