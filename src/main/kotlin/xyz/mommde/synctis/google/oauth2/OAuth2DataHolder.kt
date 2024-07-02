package xyz.mommde.synctis.google.oauth2

import io.ktor.server.auth.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Clock
import java.util.*
import kotlin.time.Duration.Companion.seconds

@Serializable
data class OAuth2DataHolder(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("refresh_token")
    val refreshToken: String? = null,
    @SerialName("exact_expiry_date")
    val expiryLocalDateTimeExact: LocalDateTime = kotlinx.datetime.Clock.System.now().plus(expiresIn.seconds).toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
)

// Will return no access Token if expired. only refresh token
fun OAuth2DataHolder.fromOAuth2DataHolder(): OAuthAccessTokenResponse.OAuth2 {
    val currentTimeStamp = kotlinx.datetime.Clock.System.now().minus(10.seconds).toLocalDateTime(TimeZone.currentSystemDefault())
    if (expiryLocalDateTimeExact <= currentTimeStamp) {
        return OAuthAccessTokenResponse.OAuth2("", tokenType, -1, refreshToken)
    }
    return OAuthAccessTokenResponse.OAuth2(accessToken, tokenType, expiresIn, refreshToken)
}

fun OAuthAccessTokenResponse.OAuth2.toOAuth2DataHolder() = OAuth2DataHolder(
    accessToken,
    tokenType,
    expiresIn,
    refreshToken
)