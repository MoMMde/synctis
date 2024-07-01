package xyz.mommde.synctis

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module
import xyz.mommde.synctis.generated.BuildConfig
import xyz.mommde.synctis.google.googleAuthenticationFileOrNull
import xyz.mommde.synctis.google.implementation.GoogleClientImpl
import xyz.mommde.synctis.google.oauth2.DiscoveryDocument
import xyz.mommde.synctis.scheduler.synchronizer
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyClient
import xyz.mommde.synctis.untis.legacy.defaultLegacyRequest
import xyz.mommde.synctis.untis.legacy.json

val webUntisModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }

            install(DefaultRequest, defaultLegacyRequest)
        }
    }

    single { WebUntisLegacyClient(get()) }
}

val googleModule = module {
    single { googleAuthenticationFileOrNull() }
    single { GoogleClientImpl(get()) }
}

suspend fun main() {
    if (Config.DEBUG)
        println("Debug enabled")
    println("Running Version: ${BuildConfig.VERSION} (${BuildConfig.GIT_SHA}; branch=${BuildConfig.GIT_BRANCH})")

    val googleCredentials = googleAuthenticationFileOrNull()
    if (googleCredentials == null) {
        println("Could not load Google Auth data. Setting up Ktor Server.")
        runKtor()
    } else {
        startKoin {
            modules(webUntisModule)
            modules(googleModule)
        }
    }
}