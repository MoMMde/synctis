package template.group.name

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

import org.slf4j.event.Level as Slf4jLevel
import org.koin.core.logger.Level as KoinLevel

import template.group.name.routing.MiscRouting

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
    log = ktorLogger
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


    install(CallLogging) {
        level = if (Config.DEBUG) Slf4jLevel.DEBUG else Slf4jLevel.INFO
        logger = ktorLogger.
        format { call ->
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val contentType = call.request.contentType()
            val queryParameters = call.request.queryParameters.toMap()
                .map { "${it.key}=${it.value.joinToString(", ")}" }

            val originHost = call.request.origin.remoteHost
            val originPort = call .request.origin.remotePort
            val status = call.response.status()
            "[$originHost:$originPort $method $path] $contentType $queryParameters ($status)"
        }
    }

    install(Koin) {
        slf4jLogger(level = if(Config.DEBUG) KoinLevel.DEBUG else KoinLevel.INFO)
        modules(repositoryModule)
    }

    routing {
        MiscRouting()
    }
}

fun runKtor() {
    embeddedServer(engine, environment()) { }.start(wait = true)
}