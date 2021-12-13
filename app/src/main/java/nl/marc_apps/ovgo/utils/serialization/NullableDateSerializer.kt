package nl.marc_apps.ovgo.utils.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@ExperimentalSerializationApi
object NullableDateSerializer : KSerializer<Date?> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = decoder.decodeDateOrNull()

    override fun serialize(encoder: Encoder, value: Date?) {
        if (value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(JsonDateTime.defaultParser.format(value))
        }
    }
}
