package xyz.mommde.synctis.scheduler

import dev.inmo.krontab.doInfinity
import io.github.oshai.kotlinlogging.KotlinLogging
import korlibs.time.days
import kotlinx.datetime.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.google.GoogleClient
import xyz.mommde.synctis.untis.WebUntisApi

private val logger = KotlinLogging.logger("SynctisScheduler")

internal suspend fun synchronizer(google: GoogleClient, untis: WebUntisApi) {
    doInfinity(Config.RUN_SCHEDULE) {
        logger.info { "Running Scheduler with config: ${Config.RUN_SCHEDULE}" }
        untis.login()
        logger.info { "Successfully logged in to WebUntis" }

        val currentInstant = Clock.System.now()
        val currentDateTime = currentInstant.toLocalDateTime(google.timeZone.invoke())
        val calendar = untis.getCalendarForWeek(currentDateTime.date)
        logger.info { "Got Calendar information from WebUntis: (events=${calendar.size})" }

        untis.logout()
        logger.info { "Logging out of WebUntis" }

        val removedResults = google.removeCalendarEventsNotInListButAreInsideDateTimeRange(
            calendar,
            currentDateTime,

            currentInstant
                .plus(Config.DAYS_IN_FUTURE.days)
                .toLocalDateTime(google.timeZone.invoke()),
        )
        logger.info { "Removed $removedResults that are not longer present in the latest update" }

        val writtenEvents = google.writeCalendarEvents(calendar)
        logger.info { "Wrote $writtenEvents to Google Calendar: ${Config.Google.CALENDAR_ID}" }
    }
}