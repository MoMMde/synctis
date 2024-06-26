package routes.misc

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import template.group.name.module
import template.group.name.routing.MiscResponse
import xyz.mommde.synctis.generated.BuildConfig
import kotlin.test.Test

class KtorTestFrameworkMiscRoute {
    @Test
    fun doesGetResponseFromMiscRoute() = testApplication {
        application {
            module()
        }

        val response = client.get("/misc")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun doesMiscInfoAgreeWithBuildConfig() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/misc")
        val body = response.body<MiscResponse>()
        assertEquals(body.version, BuildConfig.VERSION)
        assertEquals(body.gitSha, BuildConfig.GIT_SHA)
        assertEquals(body.gitBranch, BuildConfig.GIT_BRANCH)
    }
}