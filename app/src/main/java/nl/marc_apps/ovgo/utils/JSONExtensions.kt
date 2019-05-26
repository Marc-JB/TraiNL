package nl.marc_apps.ovgo.utils

import org.json.JSONArray
import org.json.JSONObject

inline fun <reified T> JSONArray.toArray(mapper: (JSONObject) -> T) =
    Array(this.length()) { mapper(this.getJSONObject(it)) }

inline fun <reified T> JSONObject.getArray(name: String, mapper: (JSONObject) -> T) =
    optJSONArray(name)?.toArray(mapper) ?: emptyArray()

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

fun JSONObject.string(name: String, fallback: String? = null): String? = optString(name, null)