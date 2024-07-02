package xyz.mommde.synctis.google.implementation.objects

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Serializable
data class GoogleCalendarSearchEventsRequestBody(
    // same as timeMin https://developers.google.com/calendar/api/v3/reference/events/list#:~:text=return%20all%20entries.-,timeMax,-datetime
    val timeMax: String,
    // rfc3339 to we have toZonedTimeStamp and store it as an string. then send it
    // https://developers.google.com/calendar/api/v3/reference/events/list#:~:text=than%20timeMin.-,timeMin,-datetime
    val timeMin: String
)

@Serializable
data class GoogleCalendarSearchedEventsResponse(
    val items: List<GoogleCalendarEvent>
)

internal fun LocalDateTime.toRFC3339String(timeZone: TimeZone): String =
    ZonedDateTime.of(toJavaLocalDateTime(), timeZone.toJavaZoneId())
        .truncatedTo(ChronoUnit.SECONDS)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"))
