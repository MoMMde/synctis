package xyz.mommde.synctis.google

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import xyz.mommde.synctis.calendar.SynctisCalendarEvent
import xyz.mommde.synctis.google.implementation.objects.GoogleCalendarEvent

interface GoogleClient {
    // https://developers.google.com/calendar/api/guides/overview
    val timeZone: suspend () -> TimeZone
    suspend fun writeCalendarEvents(events: List<SynctisCalendarEvent>): List<GoogleCalendarEvent>
    suspend fun removeCalendarEventsNotInListButAreInsideDateTimeRange(
        events: List<SynctisCalendarEvent>,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SynctisCalendarEvent>
}