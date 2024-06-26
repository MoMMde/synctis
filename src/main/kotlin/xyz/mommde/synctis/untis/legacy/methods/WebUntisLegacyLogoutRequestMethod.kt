package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyRPCRequestBuilder

internal class WebUntisLegacyLogoutRequest : WebUntisLegacyRPCRequestBuilder<Unit, Unit>("logout") {
    override val requestBuilder: HttpRequestBuilder.() -> Unit = { }
    override val body = Unit
}