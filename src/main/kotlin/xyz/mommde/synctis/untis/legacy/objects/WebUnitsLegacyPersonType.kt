package xyz.mommde.synctis.untis.legacy.objects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class WebUnitsLegacyPersonType {
    Teacher,
    Student
}

/**
 * As of official docs:
 * "type of person 2 = teacher, 5 = student"
 *
 *  https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
 *  See Section "1) Authentication"
 */
private val personTypeToIntMap = mapOf(
    2 to WebUnitsLegacyPersonType.Teacher,
    5 to WebUnitsLegacyPersonType.Student
)

fun transformIntToWebUnitsLegacyPersonType(value: Int): WebUnitsLegacyPersonType {
    return personTypeToIntMap[value] ?: throw IllegalArgumentException("Invalid WebUnitsLegacyPersonType: $value")
}

fun transformLegacyPersonTypeToInt(value: WebUnitsLegacyPersonType): Int {
    return personTypeToIntMap.firstNotNullOf { if (it.value == value) it.key else null }
}

internal object WebUnitsLegacyPersonTypeSerializer : KSerializer<WebUnitsLegacyPersonType> {
    override val descriptor = PrimitiveSerialDescriptor("WebUnitsLegacyPersonType", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): WebUnitsLegacyPersonType {
        return transformIntToWebUnitsLegacyPersonType(decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: WebUnitsLegacyPersonType) {
        encoder.encodeInt(transformLegacyPersonTypeToInt(value))
    }

}