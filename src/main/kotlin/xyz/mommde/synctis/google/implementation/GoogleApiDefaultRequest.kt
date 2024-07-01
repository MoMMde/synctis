package xyz.mommde.synctis.google.implementation

import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import xyz.mommde.synctis.Config
import xyz.mommde.synctis.google.GoogleAuthenticationData

fun defaultGoogleRequest(googleAuthenticationData: GoogleAuthenticationData): DefaultRequest.DefaultRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    url.host = "www.googleapis.com"

    bearerAuth(googleAuthenticationData.accessToken)

    userAgent(Config.USER_AGENT)
}
