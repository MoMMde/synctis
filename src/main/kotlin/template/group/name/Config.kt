package template.group.name

import dev.schlaubi.envconf.getEnv

object Config {
    val PORT by getEnv(default = 8080) { it.toInt() }
    val HOST by getEnv(default = "0.0.0.0")
    val DEBUG by getEnv(default = false) { it.toBoolean() }
}