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

object NullableDateSerializer : KSerializer<Date?> {
    private const val UTC_TIMEZONE_ID = "UTC"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ssZ"

    private const val JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    private const val JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    private val defaultParser = SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC_PRECISE, Locale.US)

    private val parsers = arrayOf(
        defaultParser,
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL_PRECISE, Locale.US),
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_UTC, Locale.US),
        SimpleDateFormat(JSON_DATE_AND_TIME_PATTERN_LOCAL, Locale.US)
    )

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    init {
        for (parser in parsers) {
            parser.calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC_TIMEZONE_ID), Locale.getDefault())
        }
    }

    @ExperimentalSerializationApi
    override fun deserialize(decoder: Decoder): Date? {
        val dateString = if (decoder.decodeNotNullMark()) {
            decoder.decodeString()
        } else {
            decoder.decodeNull()
            null
        }

        if (dateString != null) {
            for (parser in parsers) {
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

        return null
    }

    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: Date?) {
        if(value == null) {
            encoder.encodeNull()
        } else {
            encoder.encodeString(defaultParser.format(value))
        }
    }
}
