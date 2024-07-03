package xyz.mommde.synctis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import xyz.mommde.synctis.generated.BuildConfig
import xyz.mommde.synctis.google.GoogleClient
import xyz.mommde.synctis.google.googleAuthenticationFileOrNull
import xyz.mommde.synctis.google.implementation.GoogleClientImpl
import xyz.mommde.synctis.scheduler.synchronizeCalendar
import xyz.mommde.synctis.scheduler.synchronizer
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyClient

internal val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

suspend fun main() {
    if (Config.DEBUG)
        println("Debug enabled")

    val logger = KotlinLogging.logger("SynctisMainInstanceLogger")
    logger.info { "Running Version: ${BuildConfig.VERSION} (${BuildConfig.GIT_SHA}; branch=${BuildConfig.GIT_BRANCH})" }

    if (googleAuthenticationFileOrNull() == null) {
        logger.info { "Could not load Google Auth data. Setting up Ktor Server." }
        runKtor()
    } else {
        val googleClient = GoogleClientImpl()
        val webUntisApi = WebUntisLegacyClient()
        logger.info { "First run. Synchronizing Calendar" }
        synchronizeCalendar(googleClient, webUntisApi)

        logger.info {
            "Synchronization finished. Starting Scheduler with config=\"${Config.RUN_SCHEDULE}\"" +
                    " with days in future=${Config.DAYS_IN_FUTURE}"
        }
        synchronizer(googleClient, webUntisApi)
    }
}