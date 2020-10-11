package nl.marc_apps.ovgo.ui.extensions

/**
 * A combination of [Array.map] and [Array.firstOrNull].
 * Returns the result of [predicate] on the first item that is not null.
 */
inline fun <T, R> Array<out T>.takeFirstOrNull(predicate: (T) -> R?): R? {
    for (element in this) {
        val item = predicate(element)
        if(item != null) return item
    }
    return null
}