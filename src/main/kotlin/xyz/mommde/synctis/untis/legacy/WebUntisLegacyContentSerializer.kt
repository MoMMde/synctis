package xyz.mommde.synctis.untis.legacy

import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*

/**
 * Untis does not provide an Content Type Header, so Ktor does not know how to serialize an request using body()
 *
 * This is why this function forces serialization to json
 */

suspend inline fun <reified T> HttpResponse.bodyAsJson() = DefaultJson.decodeFromString<T>(bodyAsText())