package xyz.mommde.synctis.google.implementation.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleCalendarEvent(
    val id: String,
    val htmlLink: String,
    val extendedProperties: GoogleCalendarPrivateExtendedProperties.Private
)


@Serializable
@SerialName("extendedProperties")
class GoogleCalendarPrivateExtendedProperties(val private: Private) {
    @SerialName("private")
    @Serializable
    data class Private(
        val synctis: String = "Importet via Synctis. See https://github.com/MoMMde/synctis",
        val untis: String
    )
}