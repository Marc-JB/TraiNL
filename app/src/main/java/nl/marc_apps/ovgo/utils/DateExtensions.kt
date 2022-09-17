package nl.marc_apps.ovgo.utils

import android.os.Build
import androidx.annotation.IntDef
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.text.DateFormat
import java.util.*

@IntDef(DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT)
@Retention(AnnotationRetention.SOURCE)
annotation class DateTimeStyle

private fun getDefaultFormattingLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Locale.getDefault(Locale.Category.FORMAT)
    } else {
        Locale.getDefault()
    }
}

fun Instant.format(
    @DateTimeStyle dateStyle: Int? = null,
    @DateTimeStyle timeStyle: Int? = null,
    locale: Locale = getDefaultFormattingLocale(),
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
    }.format(Date.from(this.toJavaInstant()))
}
