package xyz.mommde.synctis.google.implementation.objects

import kotlinx.serialization.Serializable
import xyz.mommde.synctis.Config

@Serializable
data class GoogleCalendarInsertNewEventRequestBody(
    val start: GoogleDateTime,
    val end: GoogleDateTime,
    val colorId: String? = null,
    // https://stackoverflow.com/questions/49307014/html-formatted-text-in-google-calendar-event-description
    // https://stackoverflow.com/a/68445438/24043436
    // HOLY SHIT WE CAN HACK HTML INTO THIS
    val description: String? = null,
    val eventType: GoogleCalendarEventType = Config.Google.DEFAULT_EVENT_TYPE,
    /**
     * Auto Generate an ID for each event we create. This will allow further identication. For example the Event gets deleted
     * on Untis Platform. But if it was already synchronized, we have no idea that this shoul be deleted from Google Calendar.
     *
     * This is why we give each Event an ID to recognize it was created by our Program (Synctis).
     *
     * Google only allows characters from base32hex encoding with its length beeing between 5 and 30.
     * https://datatracker.ietf.org/doc/html/rfc2938#section-3.1.2
     *
     * We use a common beginning and then just throw some random characters at the end until we reach an length of 26
     *
     * (Wrote this before I saw 'extendedProperties' which is way more suited for this job)
     */
    val id: String = "untisintegration" + {
        val stringBuilder = StringBuilder()
        repeat(10) { stringBuilder.append((('a'..'v') + ('0'..'9')).random()) }
        stringBuilder.toString()
    },
    val extendedProperties: GoogleCalendarPrivateExtendedProperties.Private,
    val location: String? = null,
    val summary: String = "School"
)