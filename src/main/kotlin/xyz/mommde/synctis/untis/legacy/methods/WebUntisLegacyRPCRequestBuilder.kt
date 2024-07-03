package xyz.mommde.synctis.untis.legacy.methods

import io.ktor.client.request.*

internal abstract class WebUntisLegacyRPCRequestBuilder<T, V>(val method: String) {
    abstract val requestBuilder: HttpRequestBuilder.() -> Unit
    abstract val body: T
}