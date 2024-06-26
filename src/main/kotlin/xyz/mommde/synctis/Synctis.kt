package xyz.mommde.synctis

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import org.koin.dsl.module
import xyz.mommde.synctis.generated.BuildConfig
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyClient
import xyz.mommde.synctis.untis.legacy.defaultLegacyRequest

val webUntisModule = module {
    single { HttpClient(CIO) {
        install(DefaultRequest, defaultLegacyRequest)
    } }

    single { WebUntisLegacyClient(get()) }
}

fun main() {
    if (Config.DEBUG)
        println("Debug enabled")
    println("Running Version: ${BuildConfig.VERSION} (${BuildConfig.GIT_SHA}; branch=${BuildConfig.GIT_BRANCH})")
    runKtor()
}