package xyz.mommde.synctis.untis.legacy

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.pipeline.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.untis.WebUntisApi
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyAuthenticationRequest
import xyz.mommde.synctis.untis.legacy.methods.WebUntisLegacyLogoutRequest
import kotlin.math.log

/**
 * This instance uses the Legacy API provided by WebUntis
 * Docs: https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
 *
 * This should not be used anymore but we implement it anyways as it still works.
 * https://untis-sr.ch/telechargements/
 */
class WebUntisLegacyClient(
    private val client: HttpClient,
    private val school: String = Config.WebUntis.SCHOOL,
) : WebUntisApi {
    private var jSessionId: String? = null
    private val logger = KotlinLogging.logger { "WebUntisLegacyClient-${ProcessHandle.current().pid()}" }
    private suspend inline fun <reified T, reified V> sendPacket(id: String = "no_id_required", requestBuilder: WebUntisLegacyRPCRequestBuilder<T, V>): GeneralLegacyPacketResponse<V> {
        logger.info { "Sending new legacy JSON-RPC Packet. (method=${requestBuilder.method}; instance=$requestBuilder)" }

        val response = client.post {
            setBody(GeneralLegacyPacketRequest(id, requestBuilder.method, requestBuilder.body))
            contentType(ContentType.Application.Json)
            jSessionId?.let {
                cookie("JSESSIONID", it)
            }
            cookie("school", school)

            requestBuilder.requestBuilder.invoke(this)
        }

        val readBodyInSuspendedContext = response.bodyAsText()
        logger.debug { "Received response: ${response.status} $readBodyInSuspendedContext" }

        if (!response.status.isSuccess())
            throw WebUnitsLegacyRPCError("Unexpected response status: ${response.status.value}; method=${requestBuilder.method}}")

        val body = response.bodyAsJson<GeneralLegacyPacketResponse<V>>()
        if (body.error != null) {
            throw WebUnitsLegacyRPCError("""
                \"error\" path of response Json was not empty. 
                Please contact System Administrators or open an Issue on GitHub. 
                
                Method: ${requestBuilder.method}
                Message: ${body.error.message}
                Code: ${body.error.code}
                """.trimIndent())
        }
        return body
    }

    override suspend fun login(username: String, password: String): Boolean {
        logger.info { "Authenticating as $username at $school" }
        val authentication = sendPacket(requestBuilder = WebUntisLegacyAuthenticationRequest(school, username, password))
        jSessionId = authentication.result?.sessionId
        return authentication.result != null
    }

    override suspend fun logout() {
        val logout = sendPacket(requestBuilder = WebUntisLegacyLogoutRequest())
        if (logout.result != null)
            throw WebUnitsLegacyRPCError("result not null after logout. Something went wrong")

        jSessionId = null
        logger.info { "Logout successful. JSESSIONID cleared" }
    }
}

class WebUnitsLegacyRPCError(message: String) : Exception(message)

internal abstract class WebUntisLegacyRPCRequestBuilder<T, V>(val method: String) {
    abstract val requestBuilder: HttpRequestBuilder.() -> Unit
    abstract val body: T
}