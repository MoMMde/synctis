package xyz.mommde.synctis.untis.legacy

import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import xyz.mommde.synctis.Config

val defaultLegacyRequest: DefaultRequest.DefaultRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    url.host = Config.WebUntis.SERVER
    // https://untis-sr.ch/wp-content/uploads/2019/11/2018-09-20-WebUntis_JSON_RPC_API.pdf
    url.path("WebUntis", "jsonrpc.do")

    userAgent(Config.USER_AGENT)
}
