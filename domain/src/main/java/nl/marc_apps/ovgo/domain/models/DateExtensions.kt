package nl.marc_apps.ovgo.domain.models

import java.text.DateFormat
import java.util.*

fun Date.format(dateFormat: DateFormat) = dateFormat.format(this)