package xyz.mommde.synctis

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

import org.slf4j.event.Level as Slf4jLevel
import org.koin.core.logger.Level as KoinLevel

import xyz.mommde.synctis.routing.MiscRouting

private val engine = CIO
private val ktorLogger = KotlinLogging.logger { }

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

internal fun environment() = applicationEngineEnvironment {
    connector {
        port = Config.PORT
        host = Config.HOST
    }
    developmentMode = Config.DEBUG

    module {
        module()
    }
}

// to be loadable from the test framework
internal fun Application.module() {

    install(ContentNegotiation) {
        json(json)
    }

    install(Authentication) {
        oauth("google-oauth") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = Config.Google.CLIENT_ID,
                    clientSecret = Config.Google.CLIENT_SECRET,
                    defaultScopes = listOf(
                        "https://www.googleapis.com/auth/calendar",
                        "https://www.googleapis.com/auth/calendar.events"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                )
            }
            client = HttpClient {
                install(ClientContentNegotiation) {
                    json(json)
                }
            }
        }
    }

    install(CallLogging) {
        level = if (Config.DEBUG) Slf4jLevel.DEBUG else Slf4jLevel.INFO
        format { call ->
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val contentType = call.request.contentType()
            val queryParameters = call.request.queryParameters.toMap()
                .map { "${it.key}=${it.value.joinToString(", ")}" }
            val originHost = call.request.origin.remoteHost
            val originPort = call.request.origin.remotePort
            val status = call.response.status()
            "[$originHost:$originPort $method $path] $contentType $queryParameters ($status)"
        }
    }

    routing {
        MiscRouting()
        authenticate("google-oauth") {
            get("/login") {

            }
            /**
             * Get Google Access Token https://ktor.io/docs/server-oauth.html#redirect-route
             * Store it inside an file (with function next to googleAuthenticationFileOrNull)
             * Run scheduler to see when out of date. Update Automatically
             * Store Token
             * Print BIG ASS How To Message
             * When obtained start synchronizer
             * DOCKERIZE
             */
        }
    }
}

fun runKtor() {
    embeddedServer(engine, environment()) { }.start(wait = true)
}