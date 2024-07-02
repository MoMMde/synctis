package xyz.mommde.synctis.untis.legacy.objects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class WebUntisLegacyPersonType {
    Class,
    Teacher,
    Subject,
    Room,
    Student
}

/**
 * As of official docs:
 * "1 = klasse, 2 = teacher, 3 = subject, 4 = room, 5 = student"
 *
 *  https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
 *  See Section "15) Request timetable for an element (customizable)"
 */
private val personTypeToIntMap = mapOf(
    1 to WebUntisLegacyPersonType.Class,
    2 to WebUntisLegacyPersonType.Teacher,
    3 to WebUntisLegacyPersonType.Subject,
    4 to WebUntisLegacyPersonType.Room,
    5 to WebUntisLegacyPersonType.Student
)

fun transformIntToWebUnitsLegacyPersonType(value: Int): WebUntisLegacyPersonType {
    return personTypeToIntMap[value] ?: throw IllegalArgumentException("Invalid WebUnitsLegacyPersonType: $value")
}

fun transformLegacyPersonTypeToInt(value: WebUntisLegacyPersonType): Int {
    return personTypeToIntMap.firstNotNullOf { if (it.value == value) it.key else null }
}

internal object WebUnitsLegacyPersonTypeSerializer : KSerializer<WebUntisLegacyPersonType> {
    override val descriptor = PrimitiveSerialDescriptor("WebUnitsLegacyPersonType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): WebUntisLegacyPersonType {
        return transformIntToWebUnitsLegacyPersonType(decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: WebUntisLegacyPersonType) {
        encoder.encodeInt(transformLegacyPersonTypeToInt(value))
    }

}