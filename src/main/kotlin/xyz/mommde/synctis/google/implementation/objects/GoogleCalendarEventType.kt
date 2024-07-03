package xyz.mommde.synctis.google.implementation.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GoogleCalendarEventType {
    @SerialName("default")
    Default,
    @SerialName("outOfOffice")
    OutOfOffice,
    @SerialName("focusTime")
    FocusTime,
    @SerialName("workingLocation")
    WorkingLocation,
    @SerialName("fromGmail")
    FromGmail
}