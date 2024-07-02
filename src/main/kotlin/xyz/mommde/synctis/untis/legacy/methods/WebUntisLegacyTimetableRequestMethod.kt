package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.mommde.synctis.untis.legacy.objects.*
import xyz.mommde.synctis.untis.legacy.objects.WebUnitsLegacyPersonTypeSerializer
import xyz.mommde.synctis.untis.serializer.WebUntisLocalDateSerializer
import xyz.mommde.synctis.untis.serializer.WebUntisLocalTimeSerializer

@Serializable
data class WebUntisLegacyTimetableRequestBody(
    val id: Int,
    @Serializable(WebUnitsLegacyPersonTypeSerializer::class)
    val type: WebUntisLegacyPersonType,
    @Serializable(WebUntisLocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(WebUntisLocalDateSerializer::class)
    val endDate: LocalDate,
)

@Serializable
data class WebUntisLegacyTimetableResponse(
    val id: Int,
    @Serializable(WebUntisLocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("startTime")
    @Serializable(WebUntisLocalTimeSerializer::class)
    val start: LocalTime,
    @SerialName("endTime")
    @Serializable(WebUntisLocalTimeSerializer::class)
    val end: LocalTime,
    val code: String? = null,

    @SerialName("kl")
    val classes: List<WebUntisLegacyIdObject> = emptyList(),
    @SerialName("su")
    val subjects: List<WebUntisLegacyIdObject> = emptyList(),
    @SerialName("ro")
    val rooms: List<WebUntisLegacyIdObject> = emptyList(),
    @SerialName("te")
    val teachers: List<WebUntisLegacyIdObject> = emptyList(),

    val activityType: String? = null
)

internal class WebUntisLegacyTimetableRequestMethod(
    private val studentId: Int,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
) : WebUntisLegacyRPCRequestBuilder<WebUntisLegacyTimetableRequestBody, List<WebUntisLegacyTimetableResponse>>("getTimetable") {
    override val requestBuilder: HttpRequestBuilder.() -> Unit = { }
    override val body: WebUntisLegacyTimetableRequestBody = WebUntisLegacyTimetableRequestBody(
        id = studentId,
        type = WebUntisLegacyPersonType.Student,
        startDate = startDate,
        endDate = endDate,
    )
}