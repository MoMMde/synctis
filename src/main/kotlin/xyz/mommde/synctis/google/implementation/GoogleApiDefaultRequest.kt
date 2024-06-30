package xyz.mommde.synctis.google.implementation

import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import xyz.mommde.synctis.Config

val defaultGoogleRequest: DefaultRequest.DefaultRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    url.host = "www.googleapis.com"

    userAgent(Config.USER_AGENT)
}
