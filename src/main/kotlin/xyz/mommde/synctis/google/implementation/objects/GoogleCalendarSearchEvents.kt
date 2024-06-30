package xyz.mommde.synctis.google.implementation.objects

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GoogleCalendarSearchEventsRequestBody(
    val singleEvents: Boolean = true,
    val privateExtendedProperty: String = "syntic",
    val timeMin: LocalDateTime,
    val timeMax: LocalDateTime,
)

@Serializable
data class GoogleCalendarSearchedEventsResponse(
    val items: List<GoogleCalendarEvent>
)