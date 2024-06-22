package template.group.name

import template.group.`ktor-template`.generated.BuildConfig

fun main() {
    if (Config.DEBUG)
        println("Debug enabled")
    println("Running Version: ${BuildConfig.VERSION} (${BuildConfig.GIT_SHA}; branch=${BuildConfig.GIT_BRANCH})")
    runKtor(connectToMongoDB())
}