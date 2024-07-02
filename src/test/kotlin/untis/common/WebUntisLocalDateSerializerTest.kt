package untis.common

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import xyz.mommde.synctis.untis.legacy.json
import xyz.mommde.synctis.untis.serializer.WebUntisLocalDateSerializer
import xyz.mommde.synctis.untis.serializer.prefixZeroIfNotLength
import kotlin.test.Test
import kotlin.test.assertEquals

class WebUntisLocalDateSerializerTest {
    @Test
    fun `does serializer prefix with leading zeros if not long enough`() {
        assertEquals("02", "2".prefixZeroIfNotLength(2))
        assertEquals("20", "20".prefixZeroIfNotLength(2))
        assertEquals("02", "02".prefixZeroIfNotLength(2))
        assertEquals("11", "11".prefixZeroIfNotLength(2))
        assertEquals("544", "544".prefixZeroIfNotLength(3))
        assertEquals("001100", "1100".prefixZeroIfNotLength(6))
    }

    @Test
    fun `deserialize`() {
        // YYYY/MM/DD
        // 2024/06/03
        val targetDate = "20240603"
        val localDate = LocalDate(2024, 6, 3)
        assertEquals(targetDate, Json.encodeToString(WebUntisLocalDateSerializer, localDate).replace("\"", ""))
    }

    @Test
    fun `serialize`() {
        // YYYY/MM/DD
        // 2024/11/05
        val targetDate = "20241105"
        val localDate = LocalDate(2024, 11, 5)
        @Serializable
        data class LocalDateTestDataClass(@Serializable(WebUntisLocalDateSerializer::class) val date: LocalDate)
        assertEquals(localDate, json.decodeFromString<LocalDateTestDataClass>("""
            {
              "date": "$targetDate"
            }
        """.trimIndent()).date)
    }
}