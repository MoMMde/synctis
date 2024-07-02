package xyz.mommde.synctis.google.implementation

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.auth.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.google.googleAuthenticationFileOrNull

private val logger = KotlinLogging.logger("GoogleDefaultRequest")

fun defaultGoogleRequest(googleAuthenticationData: OAuthAccessTokenResponse.OAuth2? = googleAuthenticationFileOrNull()): DefaultRequest.DefaultRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    url.host = "www.googleapis.com"

    googleAuthenticationData?.accessToken?.let { bearerAuth(it) } ?: logger.warn { "No access token provided" }

    userAgent(Config.USER_AGENT)
}
