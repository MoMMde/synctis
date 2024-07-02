package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xyz.mommde.synctis.untis.legacy.objects.WebUntisLegacyPersonType
import xyz.mommde.synctis.untis.legacy.objects.WebUnitsLegacyPersonTypeSerializer

@Serializable
data class WebUntisLegacyAuthenticationRequestBody(
    @SerialName("user")
    val username: String,
    val password: String,
    val client: String
)

@Serializable
data class WebUntisLegacyAuthenticationResponse(
    val sessionId: String,
    @Serializable(with = WebUnitsLegacyPersonTypeSerializer::class)
    val personType: WebUntisLegacyPersonType,
    val personId: Int
)

internal class WebUntisLegacyAuthenticationRequest(
    private val school: String,
    private val username: String,
    private val password: String,
    private val client: String = "Synctis"
) : WebUntisLegacyRPCRequestBuilder<WebUntisLegacyAuthenticationRequestBody, WebUntisLegacyAuthenticationResponse>("authenticate") {
    override val requestBuilder: HttpRequestBuilder.() -> Unit = {
        parameter("school", school)
    }
    override val body: WebUntisLegacyAuthenticationRequestBody = WebUntisLegacyAuthenticationRequestBody(
        username = username,
        password = password,
        client = client
    )
}