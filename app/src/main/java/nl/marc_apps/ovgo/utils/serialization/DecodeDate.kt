package nl.marc_apps.ovgo.utils.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.Decoder
import java.util.*

@ExperimentalSerializationApi
fun Decoder.decodeStringOrNull(): String? {
    return if (decodeNotNullMark()) {
        decodeString()
    } else {
        decodeNull()
        null
    }
}

@ExperimentalSerializationApi
fun Decoder.decodeDateOrNull(): Date? {
    val dateString = decodeStringOrNull()

    return if (dateString != null) {
        JsonDateTime.parse(dateString)
    } else null
}
