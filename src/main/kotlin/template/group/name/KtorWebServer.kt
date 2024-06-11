package template.group.name

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import mu.KotlinLogging

private val engine = CIO
private val ktorLogger = KotlinLogging.logger("KtorTemplateLogger")

val environment = applicationEngineEnvironment {
    connector {
        port = Config.PORT
        host = Config.HOST
    }
    log = ktorLogger
    developmentMode = Config.DEBUG

    module {

    }
}

fun runKtor() {

}