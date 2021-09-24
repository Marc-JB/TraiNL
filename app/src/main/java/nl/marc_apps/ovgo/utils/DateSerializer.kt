package nl.marc_apps.ovgo.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateSerializer : KSerializer<Date> {
    private const val UTC_TIMEZONE_ID = "UTC"

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    init {
        for (parser in JsonDateTime.parsers) {
            parser.calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE_ID), Locale.getDefault())
        }
    }

    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): Date {
        val dateString = if (decoder.decodeNotNullMark()) {
            decoder.decodeString()
        } else {
            decoder.decodeNull()
            null
        }

        if (dateString != null) {
            for (parser in JsonDateTime.parsers) {
                val parsedDate = try {
                    parser.parse(dateString)
                } catch (parseException: ParseException) {
                    null
                }

                if (parsedDate != null) {
                    return parsedDate
                }
            }
        }

        return Date()
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(JsonDateTime.defaultParser.format(value))
    }
}
