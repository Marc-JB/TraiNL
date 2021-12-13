package nl.marc_apps.ovgo.utils.serialization

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object JsonDateTime {
    private const val JSON_DATE_AND_TIME_PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ssX"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSSX"

    private const val UTC_TIMEZONE_ID = "UTC"

    val defaultParser = SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE, Locale.UK)

    private val parsers by lazy {
        val defaultCalendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE_ID), Locale.UK)

        arrayOf(
            defaultParser,
            SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE, Locale.UK),
            SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC, Locale.UK),
            SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL, Locale.UK)
        ).onEach {
            it.calendar = defaultCalendar
        }
    }

    fun parse(jsonDateString: String): Date? {
        for (parser in parsers) {
            val parsedDate = try {
                parser.parse(jsonDateString)
            } catch (parseException: ParseException) {
                null
            }

            if (parsedDate != null) {
                return parsedDate
            }
        }

        return null
    }
}
