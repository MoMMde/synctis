package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyRPCRequestBuilder
import xyz.mommde.synctis.untis.legacy.objects.WebUnitsLegacyPersonType
import xyz.mommde.synctis.untis.legacy.objects.WebUnitsLegacyPersonTypeSerializer

@Serializable
data class WebUntisLegacyAuthenticationRequestBody(
    val user: String,
    val password: String,
    val client: String
)

@Serializable
data class WebUntisLegacyAuthenticationResponse(
    val sessionId: String,
    @Serializable(with = WebUnitsLegacyPersonTypeSerializer::class)
    val personType: WebUnitsLegacyPersonType,
    val personId: Int
)

internal class WebUntisLegacyAuthenticationRequest(
    private val school: String
) : WebUntisLegacyRPCRequestBuilder<WebUntisLegacyAuthenticationRequestBody, WebUntisLegacyAuthenticationResponse>("authenticate") {
    override val requestBuilder: HttpRequestBuilder.() -> Unit = {
        parameter("school", school)
    }
    override val body: WebUntisLegacyAuthenticationRequestBody = WebUntisLegacyAuthenticationRequestBody(
        user = "",
        password = "",
        client = ""
    )
}