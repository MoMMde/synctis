package xyz.mommde.synctis.untis.legacy

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.datetime.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.calendar.SynctisCalendarEvent
import xyz.mommde.synctis.untis.WebUntisApi
import xyz.mommde.synctis.untis.legacy.methods.*
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyAuthenticationRequest
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyLogoutRequest
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyRPCRequestBuilder
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyRequestSubjectsMethod
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyTimetableRequestMethod
import xyz.mommde.synctis.untis.legacy.objects.WebUntisLegacyRPCError
import java.time.temporal.TemporalAdjusters

/**
 * This instance uses the Legacy API provided by WebUntis
 * Docs: https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
 *
 * This should not be used anymore but we implement it anyways as it still works.
 * https://untis-sr.ch/telechargements/
 */
class WebUntisLegacyClient(
    private val client: HttpClient,
    private val school: String = Config.WebUntis.SCHOOL,
) : WebUntisApi {
    private var jSessionId: String? = null
    private var personId: Int? = null
    private val logger = KotlinLogging.logger { "WebUntisLegacyClient-${ProcessHandle.current().pid()}" }
    private suspend inline fun <reified T, reified V> sendPacket(
        id: String = "no_id_required",
        requestBuilder: WebUntisLegacyRPCRequestBuilder<T, V>
    ): GeneralLegacyPacketResponse<V> {
        logger.info { "Sending new legacy JSON-RPC Packet. (method=${requestBuilder.method}; instance=$requestBuilder)" }

        val response = client.post {
            setBody(GeneralLegacyPacketRequest(id, requestBuilder.method, requestBuilder.body))
            contentType(ContentType.Application.Json)
            jSessionId?.let {
                cookie("JSESSIONID", it)
            }
            cookie("school", school)

            requestBuilder.requestBuilder.invoke(this)
        }

        val readBodyInSuspendedContext = response.bodyAsText()
        logger.debug { "Received response: ${response.status} $readBodyInSuspendedContext" }

        if (!response.status.isSuccess())
            throw WebUntisLegacyRPCError("Unexpected response status: ${response.status.value}; method=${requestBuilder.method}}")

        val body = response.bodyAsJson<GeneralLegacyPacketResponse<V>>()
        if (body.error != null) {
            throw WebUntisLegacyRPCError(
                """
                \"error\" path of response Json was not empty. 
                Please contact System Administrators or open an Issue on GitHub. 
                
                Method: ${requestBuilder.method}
                Message: ${body.error.message}
                Code: ${body.error.code}
                """.trimIndent()
            )
        }
        return body
    }

    override suspend fun login(username: String, password: String): Boolean {
        logger.info { "Authenticating as $username at $school" }
        val authentication =
            sendPacket(requestBuilder = WebUntisLegacyAuthenticationRequest(school, username, password))
        jSessionId = authentication.result?.sessionId
        personId = authentication.result?.personId
        return authentication.result != null
    }

    override suspend fun logout() {
        val logout = sendPacket(requestBuilder = WebUntisLegacyLogoutRequest)
        if (logout.result != null)
            throw WebUntisLegacyRPCError("result not null after logout. Something went wrong")

        jSessionId = null
        personId = null
        logger.info { "Logout successful. JSESSIONID cleared" }
    }

    /**
     * Will return a list of CalendarEvent starting from
     * @param week
     * Ending on week + 7 days.
     * Will also freshly request getRooms & getSubjects for referencing with timetable
     */
    override suspend fun getCalendarForWeek(week: LocalDate): List<SynctisCalendarEvent> {
        if (personId == null)
            throw IllegalStateException("Not logged in. Please call login() first")

        val monday = week.toJavaLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toKotlinLocalDate()
        val rooms = getRooms()
        logger.info { "Loaded ${rooms?.size} rooms for referencing with Timetable" }

        val subjects = getSubjects()
        logger.info { "Loaded ${subjects?.size} subjects for referencing with Timetable" }

        val timetableMethod = WebUntisLegacyTimetableRequestMethod(personId!!, monday, monday.plus(DatePeriod(days = 7)))
        val timetable = sendPacket(requestBuilder = timetableMethod).result!!

        return timetable.map { subjectIdObject ->
            if (subjectIdObject.subjects.size > 1)
                throw IllegalStateException("There are more than two subjects in one Timetable Entry List")

            val subjectId = subjectIdObject.subjects.firstOrNull()?.id
            val subject = subjects?.firstOrNull { it.id == subjectId }

            val roomId = subjectIdObject.rooms.firstOrNull()?.id
            val room = rooms?.firstOrNull { it.id == roomId }

            val renderedRoom = if (Config.WebUntis.SCHOOL_LOCATION.isEmpty())
                room?.name
            else
                Config.WebUntis.SCHOOL_LOCATION + ", " + room!!.name

            SynctisCalendarEvent(
                start = subjectIdObject.start.atDate(subjectIdObject.date),
                end = subjectIdObject.end.atDate(subjectIdObject.date),
                color = subject?.backgroundColor,
                location = renderedRoom,
                name = subject?.longName ?: "No name for SubjectId: $subjectId",
                teacher = "",
                homework = "",
                id = subjectIdObject.id.toString(),
            )
        }
    }


    private suspend fun getSubjects() = sendPacket(requestBuilder = WebUntisLegacyRequestSubjectsMethod).result

    private suspend fun getRooms() = sendPacket(requestBuilder = WebUntisLegacyRequestRoomsMethod).result
}