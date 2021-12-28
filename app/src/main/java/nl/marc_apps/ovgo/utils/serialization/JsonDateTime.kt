package nl.marc_apps.ovgo.utils.serialization

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object JsonDateTime {
    private const val JSON_DATE_AND_TIME_PATTERN_BASE = "yyyy-MM-dd'T'HH:mm:ss"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC = "${JSON_DATE_AND_TIME_PATTERN_BASE}'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL = "${JSON_DATE_AND_TIME_PATTERN_BASE}X"
    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_COMPAT = "${JSON_DATE_AND_TIME_PATTERN_BASE}ZZZZZ"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE = "${JSON_DATE_AND_TIME_PATTERN_BASE}.SSS'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE = "${JSON_DATE_AND_TIME_PATTERN_BASE}.SSSX"
    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE_COMPAT = "${JSON_DATE_AND_TIME_PATTERN_BASE}.SSSZZZZZ"

    private const val UTC_TIMEZONE_ID = "UTC"

    val defaultParser = SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE, Locale.UK)

    @SuppressLint("NewApi")
    private fun createDateFormatWithFallback(defaultPattern: String, fallbackPattern: String, locale: Locale): SimpleDateFormat {
        return try {
            SimpleDateFormat(defaultPattern, locale)
        } catch (error: Throwable) {
            SimpleDateFormat(fallbackPattern, locale)
        }
    }

    private val parsers by lazy {
        val defaultCalendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE_ID), Locale.UK)

        arrayOf(
            defaultParser,
            createDateFormatWithFallback(
                JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE,
                JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE_COMPAT,
                Locale.UK
            ),
            SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC, Locale.UK),
            createDateFormatWithFallback(
                JSON_DATE_AND_TIME_PATTERN_LOCAL,
                JSON_DATE_AND_TIME_PATTERN_LOCAL_COMPAT,
                Locale.UK
            )
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
