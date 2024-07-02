package xyz.mommde.synctis.untis.legacy

import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import xyz.mommde.synctis.json

/**
 * Untis does not provide an Content Type Header, so Ktor does not know how to serialize an request using body()
 *
 * This is why this function forces serialization to json
 */
internal suspend inline fun <reified T> HttpResponse.bodyAsJson() = json.decodeFromString<T>(bodyAsText())