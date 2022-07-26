package nl.marc_apps.ovgo.data.api.dutch_railways.utils

import android.util.Log
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object InstantIso8601Serializer: KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        val timestamp = decoder.decodeString()
        return try {
            Instant.parse(fixOffsetRepresentation(timestamp))
        } catch (error: IllegalArgumentException) {
            Log.e("InstantIso8601Serializer", "timestamp: $timestamp", error)
            Instant.parse("2020-01-01T00:00Z")
        }
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    private fun fixOffsetRepresentation(isoString: String): String {
        val time = isoString.indexOf('T', ignoreCase = true)
        if (time == -1) return isoString // the string is malformed
        val offset = isoString.indexOfLast { c -> c == '+' || c == '-' }
        if (offset < time) return isoString // the offset is 'Z' and not +/- something else
        val separator = isoString.indexOf(':', offset) // if there is a ':' in the offset, no changes needed
        return when {
            separator != -1 -> isoString
            isoString.length - offset == 5 -> "${isoString.substring(0, isoString.length - 2)}:${isoString.substring(isoString.length - 2)}"
            else -> "$isoString:00"
        }
    }
}
