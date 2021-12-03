package nl.marc_apps.ovgo.utils

import java.text.DateFormat
import java.util.*
import kotlin.test.*

class DataExtensionsTests {
    @Test
    fun formatWithDateOnlyWillReturnDateStringWithoutTime() {
        // Arrange
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(dateStyle = DateFormat.FULL, locale = locale, timeZone = timeZone)

        // Assert
        println(formattedDate)
        assertFalse("16" in formattedDate)
        assertFalse("35" in formattedDate)
        assertFalse(":" in formattedDate)
        assertTrue("22" in formattedDate)
        assertTrue("2021" in formattedDate)
    }

    @Test
    fun formatWithTimeOnlyWillReturnTimeStringWithoutDate() {
        // Arrange
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(timeStyle = DateFormat.FULL, locale = locale, timeZone = timeZone)

        // Assert
        println(formattedDate)
        assertTrue("16" in formattedDate)
        assertTrue("35" in formattedDate)
        assertTrue(":" in formattedDate)
        assertFalse("22" in formattedDate)
        assertFalse("2021" in formattedDate)
    }

    @Test
    fun formatWithDateAndTimeWillReturnStringWithBothDateAndTime() {
        // Arrange
        val timeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(timeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(dateStyle = DateFormat.FULL, timeStyle = DateFormat.FULL, locale = locale, timeZone = timeZone)

        // Assert
        println(formattedDate)
        assertTrue("16" in formattedDate)
        assertTrue("35" in formattedDate)
        assertTrue(":" in formattedDate)
        assertTrue("22" in formattedDate)
        assertTrue("2021" in formattedDate)
    }

    @Test
    fun formatWithUtcTimeAndCetTimeZoneWillReturnCetTime() {
        // Arrange
        val inputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val outputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_CET)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(inputTimeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(timeStyle = DateFormat.FULL, locale = locale, timeZone = outputTimeZone)

        // Assert
        println(formattedDate)
        assertFalse("16" in formattedDate)
        assertTrue("17" in formattedDate)
        assertTrue("35" in formattedDate)
    }

    @Test
    fun formatWithUtcTimeAndCestTimeZoneWillReturnCestTime() {
        // Arrange
        val inputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val outputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_CEST)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(inputTimeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(timeStyle = DateFormat.FULL, locale = locale, timeZone = outputTimeZone)

        // Assert
        println(formattedDate)
        assertFalse("16" in formattedDate)
        assertTrue("18" in formattedDate)
        assertTrue("35" in formattedDate)
    }

    @Test
    fun formatWithCetTimeAndUtcTimeZoneWillReturnUtcTime() {
        // Arrange
        val inputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_CET)
        val outputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(inputTimeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(timeStyle = DateFormat.FULL, locale = locale, timeZone = outputTimeZone)

        // Assert
        println(formattedDate)
        assertFalse("16" in formattedDate)
        assertTrue("15" in formattedDate)
        assertTrue("35" in formattedDate)
    }

    @Test
    fun formatWithCestTimeAndUtcTimeZoneWillReturnUtcTime() {
        // Arrange
        val inputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_CEST)
        val outputTimeZone = TimeZone.getTimeZone(TIMEZONE_ID_UTC)
        val locale = Locale.UK
        val calendar = GregorianCalendar.getInstance(inputTimeZone, locale)
        calendar.set(2021, Calendar.DECEMBER, 22, 16, 35)
        val date = Date.from(calendar.toInstant())

        // Act
        val formattedDate = date.format(timeStyle = DateFormat.FULL, locale = locale, timeZone = outputTimeZone)

        // Assert
        println(formattedDate)
        assertFalse("16" in formattedDate)
        assertTrue("14" in formattedDate)
        assertTrue("35" in formattedDate)
    }

    companion object {
        private const val TIMEZONE_ID_UTC = "GMT"

        private const val TIMEZONE_ID_CET = "GMT+1"

        private const val TIMEZONE_ID_CEST = "GMT+2"
    }
}
