package nl.marc_apps.ovgo.utils.serialization

import java.util.*
import kotlin.test.*

class JsonDateTimeParsingTests {
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(16, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(16, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
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
        assertEquals(2021, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(2, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(49, calendar.get(Calendar.SECOND))
    }

    companion object {
        private const val TIMEZONE_ID_UTC = "GMT"
    }
}
