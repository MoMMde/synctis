package xyz.mommde.synctis.google.implementation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.serialization.encodeToString
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.calendar.SynctisCalendarEvent
import xyz.mommde.synctis.google.GoogleClient
import xyz.mommde.synctis.google.googleAuthenticationFileOrNull
import xyz.mommde.synctis.google.implementation.objects.*
import xyz.mommde.synctis.google.oauth2.OAuth2DataHolder
import xyz.mommde.synctis.google.oauth2.fromOAuth2DataHolder
import xyz.mommde.synctis.google.writeOauth2Token
import xyz.mommde.synctis.json
import xyz.mommde.synctis.runKtor
import xyz.mommde.synctis.untis.mergeEvents

class GoogleClientImpl : GoogleClient {
    private val client = HttpClient(CIO) {
        install(DefaultRequest, defaultGoogleRequest())
        install(ContentNegotiation) {
            json(json)
        }
    }

    // https://developers.google.com/calendar/api/v3/reference/calendars/get
    override val timeZone: TimeZone
        get() = runBlocking {
            val response = client.get("calendar/v3/calendars/${Config.Google.CALENDAR_ID}") {
                googleAuthenticationFileOrNull()?.accessToken?.let { bearerAuth(it) }
            }
            logger.debug { "Fetched time zone from calendar." }
            response.body<GoogleCalendar>().timeZone
        }

    private val logger = KotlinLogging.logger { "GoogleCalendarEventApi-${ProcessHandle.current().pid()}" }
    private suspend fun writeSingleCalendarEvent(event: SynctisCalendarEvent): GoogleCalendarEvent {
        val response = client.post("/calendar/v3/calendars/${Config.Google.CALENDAR_ID}/events") {
            setBody(
                GoogleCalendarInsertNewEventRequestBody(
                    start = event.start.toGoogleDateTime(timeZone),
                    end = event.end.toGoogleDateTime(timeZone),
                    description = """
                    <hr>
                    <h2>Homework:</h2>
                    <ul><li>${event.homework ?: "None"}</li></ul>
                    <hr>
                    <h2>Teacher:</h2>
                    <ul><li>${event.teacher ?: "N/A"}</li></ul>
                    <hr>
                    <h2>Details:</h2>
                    <hr>
                """.trimIndent(),
                    location = event.location,
                    summary = event.name ?: "School",
                    extendedProperties = GoogleCalendarPrivateExtendedProperties(GoogleCalendarPrivateExtendedProperties.Private(untis = event.name + event.id))
                )
            )
            contentType(ContentType.Application.Json)
        }
        val bodyAsTextWithSuspendedContext = response.bodyAsText()
        logger.debug { "Response from insertion into Google Calendar: $bodyAsTextWithSuspendedContext" }
        val serializedBody = response.body<GoogleCalendarEvent>()
        logger.info { "Inserted new event: ${serializedBody.id}. See online under: ${serializedBody.htmlLink}" }
        return serializedBody
    }

    override suspend fun refreshToken(): Boolean {
        logger.info { "Checking if token still valid." }
        val googleAuthenticationData = googleAuthenticationFileOrNull() ?: return false
        println(googleAuthenticationData)
        if (googleAuthenticationData.expiresIn != (-1).toLong()) {
            logger.info { "Token still valid." }
            return false
        }
        if (googleAuthenticationData.refreshToken.isNullOrEmpty()) {
            logger.info { "Please Re-Authenticate as no refresh token is provided" }
            runKtor()
            return false
        }

        logger.info { "Token no longer valid. Requesting new one." }
        val newToken = client.post("https://oauth2.googleapis.com/token") {
            parameter("client_id", Config.Google.CLIENT_ID)
            parameter("client_secret", Config.Google.CLIENT_SECRET)
            parameter("refresh_token", googleAuthenticationData.refreshToken)
            parameter("grant_type", "refresh_token")
        }.also { println(it.bodyAsText()) }.body<OAuth2DataHolder>()

        logger.info { "Got new access token." }
        writeOauth2Token(oauthToken = newToken.fromOAuth2DataHolder().copy(refreshToken = googleAuthenticationData.refreshToken))
        return true
    }

    override suspend fun writeCalendarEvents(event: List<SynctisCalendarEvent>): List<GoogleCalendarEvent> {
        if (event.isEmpty())
            return emptyList()
        val minimumDate = event.map { it.start }.minBy { it }
        val maximumDate = event.map { it.end }.maxBy { it }
        val filterEvents = getCalendarEventsInDateTimeRange(minimumDate, maximumDate)
        return event.filterNot { filteredEvent ->
            filteredEvent.id in filterEvents.body<GoogleCalendarSearchedEventsResponse>().items.map { it.id }
        }.map { writeSingleCalendarEvent(it) }
    }

    private suspend fun removeCalendarEvent(calendarId: String = Config.Google.CALENDAR_ID, eventId: String) {
        client.delete("https://www.googleapis.com/calendar/v3/calendars/$calendarId/events/$eventId")
    }

    private suspend fun getCalendarEventsInDateTimeRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): HttpResponse {
        val timeZone = timeZone
        logger.debug { "timeMax=${endDate.toRFC3339String(timeZone)} - timeMin=${startDate.toRFC3339String(timeZone)}" }
        return client.get("/calendar/v3/calendars/${Config.Google.CALENDAR_ID}/events") {
            parameter("timeMax", endDate.toRFC3339String(timeZone))
            parameter("timeMin", startDate.toRFC3339String(timeZone))
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun removeCalendarEventsNotInListButAreInsideDateTimeRange(
        events: List<SynctisCalendarEvent>,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<String> {
        logger.debug { "Requesting events from $startDate to $endDate" }
        val response = getCalendarEventsInDateTimeRange(startDate, endDate)
        val bodyAsTextWithSuspendedContext = response.bodyAsText()
        logger.debug { "Received filter search answer: $bodyAsTextWithSuspendedContext" }

        val eventsThatShouldBeInThereAsIdList = events.map { it.name + it.id }
        val removedEvents = mutableListOf<String>()
        response.body<GoogleCalendarSearchedEventsResponse>()
            .items
            .filter { it.extendedProperties.private?.synctis != null }
            .forEach { toBeRemovedEvent ->
                if(!eventsThatShouldBeInThereAsIdList.contains(toBeRemovedEvent.extendedProperties.private?.untis)) {
                    logger.info { "Removing Event as of disagreement in removeCalendarEventsNotInListButAreInsideDateTimeRange. ${toBeRemovedEvent.id}" }
                    removeCalendarEvent(eventId = toBeRemovedEvent.id)
                } else {
                    // to mark an event that exists as existing so it will be removed from to "to be synced" list
                    // in the synchronizer
                    removedEvents.add(toBeRemovedEvent.extendedProperties.private!!.untis!!)
                }
            }
        return removedEvents
    }
}