package xyz.mommde.synctis.untis.legacy

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import xyz.mommde.synctis.untis.WebUntisApi
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyAuthenticationRequest

/**
 * This instance uses the Legacy API provided by WebUntis
 * Docs: https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
 *
 * This should not be used anymore but we implement it anyways as it still works.
 * https://untis-sr.ch/telechargements/
 */
class WebUntisLegacyClient(
    private val client: HttpClient
) : WebUntisApi {
    private var jSessionId: String? = null
    private suspend inline fun <reified T, reified V> sendPacket(id: String = "no_id_required", requestBuilder: WebUntisLegacyRPCRequestBuilder<T, V>): GeneralLegacyPacketResponse<V> {
        val response = client.post {
            setBody(GeneralLegacyPacketRequest(id, requestBuilder.method, requestBuilder.body))
        }
        if (!response.status.isSuccess())
            throw IllegalStateException("Unexpected response status: ${response.status.value}; method=${requestBuilder.method}}")
        return response.body()
    }

    override suspend fun login(): Boolean {
        val authentication = sendPacket(requestBuilder = WebUntisLegacyAuthenticationRequest("asdf"))
        jSessionId = authentication.result?.sessionId
        return authentication.result != null
    }

    override suspend fun logout() {

    }
}

internal abstract class WebUntisLegacyRPCRequestBuilder<T, V>(val method: String) {
    abstract val requestBuilder: HttpRequestBuilder.() -> Unit
    abstract val body: T
}