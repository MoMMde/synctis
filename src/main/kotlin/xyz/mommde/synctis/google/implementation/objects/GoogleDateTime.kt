package xyz.mommde.synctis.google.implementation.objects

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import xyz.mommde.synctis.Config

@Serializable
data class GoogleDateTime(
    val date: LocalDate,
    val dateTime: LocalDateTime,
    val timeZone: TimeZone
)

fun LocalDateTime.toGoogleDateTime(timeZone: TimeZone): GoogleDateTime = GoogleDateTime(
    date = date,
    dateTime = this,
    timeZone = timeZone
)