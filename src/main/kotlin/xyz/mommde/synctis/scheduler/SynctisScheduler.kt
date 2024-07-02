package xyz.mommde.synctis.scheduler

import dev.inmo.krontab.doInfinity
import io.github.oshai.kotlinlogging.KotlinLogging
import korlibs.time.days
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.google.GoogleClient
import xyz.mommde.synctis.untis.WebUntisApi
import xyz.mommde.synctis.untis.mergeEvents

private val logger = KotlinLogging.logger("SynctisScheduler")

internal fun synchronizer(google: GoogleClient, untis: WebUntisApi) = runBlocking {
    doInfinity(Config.RUN_SCHEDULE) {
        logger.info { "Running Scheduler with config: ${Config.RUN_SCHEDULE}" }
        synchronizeCalendar(google, untis)
    }
}

internal suspend fun synchronizeCalendar(google: GoogleClient, untis: WebUntisApi) {
    google.refreshToken()
    untis.login()
    logger.info { "Successfully logged in to WebUntis" }

    val currentInstant = Clock.System.now()
    val currentDateTime = currentInstant.toLocalDateTime(google.timeZone)
    val calendar = untis.getCalendarForWeek(currentDateTime.date)
    logger.info { "Got Calendar information from WebUntis: (events=${calendar.size})" }

    var mergedCalendar = mergeEvents(calendar)
    logger.info { "Merged events that follow each other" }

    untis.logout()
    logger.info { "Logging out of WebUntis" }

    val removedResults = google.removeCalendarEventsNotInListButAreInsideDateTimeRange(
        mergedCalendar,
        currentDateTime,

        currentInstant
            .plus(Config.DAYS_IN_FUTURE.days)
            .toLocalDateTime(google.timeZone),
    )
    mergedCalendar = mergedCalendar.filter { !removedResults.contains(it.name + it.id) }
    mergedCalendar = mergedCalendar.filter { it.start.toInstant(google.timeZone) > Clock.System.now() }
    logger.info { "Detected ${removedResults.size} that did not change" }

    val writtenEvents = google.writeCalendarEvents(mergedCalendar)
    logger.info { "Wrote ${writtenEvents.size} to Google Calendar: ${Config.Google.CALENDAR_ID}" }
}