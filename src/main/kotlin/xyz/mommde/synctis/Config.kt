package xyz.mommde.synctis

import dev.schlaubi.envconf.Config as EnvConf
import dev.schlaubi.envconf.Environment
import dev.schlaubi.envconf.getEnv
import xyz.mommde.synctis.generated.BuildConfig

object Config {
    val PORT by getEnv(default = 8080) { it.toInt() }
    val HOST by getEnv(default = "0.0.0.0")
    val DEBUG by getEnv(default = false) { it.toBoolean() }

    object WebUntis : EnvConf("UNTIS_") {
        val SERVER by getEnv()
        val USERNAME by getEnv()
        val PASSWORD by getEnv()
        val SCHOOL by getEnv()
    }

    val USER_AGENT by getEnv(default = "Synctis@${BuildConfig.VERSION} (${BuildConfig.GIT_SHA})")
}