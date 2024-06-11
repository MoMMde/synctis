package template.group.name

import dev.schlaubi.envconf.getEnv
import java.lang.System.getenv

object Config {
    val PORT by getEnv(default = 8080) { it.toInt() }
    val HOST by getEnv(default = "0.0.0.0")
    val DEBUG by getEnv(default = false) { it.toBoolean() }
    val MONGO_URL by getEnv()
}