package xyz.mommde.synctis.calendar

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
data class SynctisCalendarEvent(
    val id: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val name: String = "School",
    val location: String? = null,
    val color: String? = null,
    val teacher: String? = null,
    val homework: String? = null
)