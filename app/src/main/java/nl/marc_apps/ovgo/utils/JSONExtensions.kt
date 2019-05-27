package nl.marc_apps.ovgo.utils

import org.json.JSONObject

/**
 * Returns the value mapped by [name] if it exists, coercing it if
 * necessary, or [fallback] if no such mapping exists.
 */
fun JSONObject.int(name: String, fallback: Int? = null): Int? {
    val value = opt(name)
    when (value) {
        is Int -> return value
        is Number -> return value.toInt()
        is String -> try {
            return java.lang.Double.parseDouble(value).toInt()
        } catch (ignored: NumberFormatException) {
        }
    }
    return fallback
}

/**
 * Returns the value mapped by [name] if it exists, coercing it if
 * necessary, or [fallback] if no such mapping exists.
 */
fun JSONObject.string(name: String, fallback: String? = null): String? = optString(name, null)