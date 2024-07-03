package xyz.mommde.synctis

import dev.schlaubi.envconf.Config as EnvConf
import dev.schlaubi.envconf.Environment
import dev.schlaubi.envconf.getEnv
import xyz.mommde.synctis.generated.BuildConfig
import xyz.mommde.synctis.google.implementation.objects.GoogleCalendarEventType

object Config {
    val PORT by getEnv(default = 8080) { it.toInt() }
    val HOST by getEnv(default = "localhost")
    val HOST_KTOR by getEnv(default = "localhost")
    val DEBUG by getEnv(default = false) { it.toBoolean() }

    object WebUntis : EnvConf("UNTIS_") {
        val SERVER by getEnv()
        val USERNAME by getEnv()
        val PASSWORD by getEnv()
        val SCHOOL by getEnv()
        val SCHOOL_LOCATION by getEnv(default = "")
    }

    object Google : EnvConf("GOOGLE_") {
        val AUTH_FILE by getEnv(default = ".google-auth")
        val CALENDAR_ID by getEnv()
        val DEFAULT_COLOR_ID by getEnv(default = "")
        val DEFAULT_EVENT_TYPE by getEnv(default = GoogleCalendarEventType.Default) { GoogleCalendarEventType.valueOf(it.uppercase()) }

        val CLIENT_ID by getEnv()
        val CLIENT_SECRET by getEnv()
    }

    val USER_AGENT by getEnv(default = "Synctis@${BuildConfig.VERSION} (${BuildConfig.GIT_SHA})")
    // https://crontab.guru/#0_5_1-31_*_*
    // Run at 5 in the morning every day
    val RUN_SCHEDULE by getEnv(default = "0 5 1-31 * *")
    val WEEKS_IN_FUTURE by getEnv(default = 2) { if (it.toInt() < 1) 1 else it.toInt() }
}