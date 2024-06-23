package template.group.name

import com.mongodb.kotlin.client.coroutine.MongoClient
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
import mu.KotlinLogging
import org.slf4j.event.Level
import template.group.name.routing.MiscRouting

private val engine = CIO
private val ktorLogger = KotlinLogging.logger("KtorTemplateLogger")
private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

internal fun environment(mongoClient: MongoClient) = applicationEngineEnvironment {
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
        level = if (Config.DEBUG) Level.DEBUG else Level.INFO
        logger = ktorLogger
        format { call ->
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val contentType = call.request.contentType()
            val queryParameters = call.request.queryParameters.toMap().mapKeys { "${it.key}=${it.value}" }
            val originHost = call.request.origin.remoteHost
            val originPort = call.request.origin.remotePort
            val status = call.response.status()
            "[$originHost:$originPort $method $path] $contentType $queryParameters ($status)"
        }
    }

    routing {
        MiscRouting()
    }
}

fun runKtor(mongoClient: MongoClient) {
    embeddedServer(engine, environment(mongoClient)) { }.start(wait = true)
}