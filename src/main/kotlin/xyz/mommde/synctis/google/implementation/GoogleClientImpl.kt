package xyz.mommde.synctis.google.implementation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.auth.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.calendar.SynctisCalendarEvent
import xyz.mommde.synctis.google.GoogleClient
import xyz.mommde.synctis.google.implementation.objects.*

class GoogleClientImpl(val googleAuthenticationData: OAuthAccessTokenResponse.OAuth2) : GoogleClient {
    private val client = HttpClient(CIO) {
        install(DefaultRequest, defaultGoogleRequest(googleAuthenticationData))
    }
    // https://developers.google.com/calendar/api/v3/reference/calendars/get
    override val timeZone: suspend () -> TimeZone
        get() = {
            val response = client.get("calendar/v3/calendars/${Config.Google.CALENDAR_ID}")
            response.body<GoogleCalendar>().timeZone
        }
    private val logger = KotlinLogging.logger { "GoogleCalendarEventApi-${ProcessHandle.current().pid()}" }
    private suspend fun writeSingleCalendarEvent(event: SynctisCalendarEvent): GoogleCalendarEvent {
        val timeZone = timeZone.invoke()
        val response = client.post("/calendar/v3/calendars/${Config.Google.CALENDAR_ID}/events") {
            setBody(GoogleCalendarInsertNewEventRequestBody(
                start = event.start.toGoogleDateTime(timeZone),
                end = event.end.toGoogleDateTime(timeZone),
                description = """
                    <h2>Html Test</h2>
                """.trimIndent(),
                location = event.location,
                summary = event.name,
                id = event.id,
                extendedProperties = GoogleCalendarPrivateExtendedProperties.Private(untis = event.name)
            ))
        }
        val bodyAsTextWithSuspendedContext = response.bodyAsText()
        logger.debug { "Response from insertion into Google Calendar: $bodyAsTextWithSuspendedContext" }
        val serializedBody = response.body<GoogleCalendarEvent>()
        logger.info { "Inserted new event: ${serializedBody.id}. See online under: ${serializedBody.htmlLink}" }
        return serializedBody
    }

    override suspend fun writeCalendarEvents(event: List<SynctisCalendarEvent>): List<GoogleCalendarEvent> {
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
        return client.get("/calendar/v3/calendars/${Config.Google.CALENDAR_ID}/events") {
            setBody(GoogleCalendarSearchEventsRequestBody(
                timeMax = startDate,
                timeMin = endDate,
            ))
        }
    }

    override suspend fun removeCalendarEventsNotInListButAreInsideDateTimeRange(
        events: List<SynctisCalendarEvent>,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SynctisCalendarEvent> {
        val response = getCalendarEventsInDateTimeRange(startDate, endDate)
        val bodyAsTextWithSuspendedContext = response.bodyAsText()
        logger.debug { "Received filter search answer: $bodyAsTextWithSuspendedContext" }

        val eventsThatShouldBeInThereAsIdList = events.map { it.id }
        val removedEvents = mutableListOf<SynctisCalendarEvent>()
        response.body<GoogleCalendarSearchedEventsResponse>()
            .items
            .filterNot {
                it.id in eventsThatShouldBeInThereAsIdList
            }
            .forEach { toBeRemovedEvent ->
                logger.info { "Removing Event as of disagreement in removeCalendarEventsNotInListButAreInsideDateTimeRange. ${toBeRemovedEvent.id}" }
                removeCalendarEvent(eventId = toBeRemovedEvent.id)
                removedEvents.add(events.first { it.id == toBeRemovedEvent.id })
            }
        return removedEvents
    }
}