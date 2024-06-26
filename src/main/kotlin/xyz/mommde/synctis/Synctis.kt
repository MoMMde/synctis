package xyz.mommde.synctis

import xyz.mommde.synctis.generated.BuildConfig

fun main() {
    if (Config.DEBUG)
        println("Debug enabled")
    println("Running Version: ${BuildConfig.VERSION} (${BuildConfig.GIT_SHA}; branch=${BuildConfig.GIT_BRANCH})")
    runKtor()
}