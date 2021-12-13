package nl.marc_apps.ovgo.utils

import androidx.annotation.IntDef
import java.text.DateFormat
import java.util.*

@IntDef(DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT)
@Retention(AnnotationRetention.SOURCE)
annotation class DateTimeStyle

fun Date.format(
    @DateTimeStyle dateStyle: Int? = null,
    @DateTimeStyle timeStyle: Int? = null,
    locale: Locale = Locale.getDefault(Locale.Category.FORMAT),
    calendar: Calendar? = null,
    timeZone: TimeZone? = null
): String {
    return when {
        dateStyle == null && timeStyle != null -> DateFormat.getTimeInstance(timeStyle, locale)
        dateStyle != null && timeStyle == null -> DateFormat.getDateInstance(dateStyle, locale)
        dateStyle != null && timeStyle != null -> DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale)
        else -> throw IllegalStateException("TimeStyle and DateStyle can't be both null")
    }.also {
        if (calendar != null) {
            it.calendar = calendar
        }

        if (timeZone != null) {
            it.timeZone = timeZone
        }
    }.format(this)
}
