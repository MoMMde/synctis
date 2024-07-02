package xyz.mommde.synctis.untis.serializer

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object WebUntisLocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime {
        val timeString = decoder.decodeInt().toString()
        val hoursEndIndex = if(timeString.length == 4) 2 else 1
        return LocalTime(
            hour = timeString.substring(0, hoursEndIndex).toInt(),
            minute = timeString.substring(hoursEndIndex, timeString.length).toInt(),
            second = 0,
            nanosecond = 0
        )
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(
            value.hour.toString() +
                    value.minute.toString().prefixZeroIfNotLength(requiredLength = 2)
        )
    }
}

object WebUntisLocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        val dateString = decoder.decodeInt().toString()
        return LocalDate(
            dateString
                .substring(0, 4)
                .toInt(),

            dateString
                .substring(4, 6)
                .toInt(),

            dateString
                .substring(6, dateString.length)
                .toInt()
        )
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(
            value.year.toString().prefixZeroIfNotLength(requiredLength = 4) +
                    value.monthNumber.toString().prefixZeroIfNotLength(requiredLength = 2) +
                    value.dayOfMonth.toString().prefixZeroIfNotLength(requiredLength = 2)
        )
    }
}

fun String.prefixZeroIfNotLength(requiredLength: Int): String {
    return "0".repeat(requiredLength - length) + this
}