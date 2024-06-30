package xyz.mommde.synctis.google.implementation.objects

import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

@Serializable
data class GoogleCalendar(
    val id: String,
    val timeZone: TimeZone
)
