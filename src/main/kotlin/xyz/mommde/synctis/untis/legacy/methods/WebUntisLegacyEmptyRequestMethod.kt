package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*

internal open class WebUntisLegacyEmptyRequestMethod<V>(method: String) : WebUntisLegacyRPCRequestBuilder<Unit, V>(method) {
    override val requestBuilder: HttpRequestBuilder.() -> Unit = { }
    override val body: Unit = Unit
}