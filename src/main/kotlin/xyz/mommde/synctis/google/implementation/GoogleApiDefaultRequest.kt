package xyz.mommde.synctis.google.implementation

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.auth.*
import xyz.mommde.synctis.Config

fun defaultGoogleRequest(googleAuthenticationData: OAuthAccessTokenResponse.OAuth2): DefaultRequest.DefaultRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    url.host = "www.googleapis.com"

    bearerAuth(googleAuthenticationData.accessToken)

    userAgent(Config.USER_AGENT)
}
