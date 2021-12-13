package nl.marc_apps.ovgo.utils.serialization

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JsonDateTimeParsingTests {
    private fun assertCalendarEquals(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        hourOfDay: Int,
        minute: Int,
        second: Int,
        calendar: Calendar
    ) {
        assertEquals(year, calendar.get(Calendar.YEAR))
        assertEquals(month, calendar.get(Calendar.MONTH))
        assertEquals(dayOfMonth, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(hourOfDay, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(minute, calendar.get(Calendar.MINUTE))
        assertEquals(second, calendar.get(Calendar.SECOND))
    }

    @Test
    fun parsingJsonUtcTimestampShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49Z"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 16, 35, 49, calendar)
    }

    @Test
    fun parsingJsonCetTimestampShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49+01:00"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 15, 35, 49, calendar)
    }

    @Test
    fun parsingJsonCestTimestampShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49+02:00"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 14, 35, 49, calendar)
    }
    @Test
    fun parsingJsonUtcTimestampWithMillisecondsShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49.827Z"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 16, 35, 49, calendar)
    }

    @Test
    fun parsingJsonCetTimestampWithMillisecondsShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49.827+01:00"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 15, 35, 49, calendar)
    }

    @Test
    fun parsingJsonCestTimestampWithMillisecondsShouldReturnCorrectDate() {
        // Arrange
        val timestamp = "2021-12-02T16:35:49.827+02:00"
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)

        // Act
        val parsedDate = JsonDateTime.parse(timestamp)

        // Assert
        assertNotNull(parsedDate)
        calendar.time = parsedDate
        assertCalendarEquals(2021, Calendar.DECEMBER, 2, 14, 35, 49, calendar)
    }

    companion object {
        private const val TIMEZONE_ID_UTC = "GMT"
    }
}
