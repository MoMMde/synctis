package routes.misc

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import org.junit.Test
import template.group.name.module

class KtorTestFrameworkMiscRoute {
    @Test
    fun doesGetResponseFromMiscRoute() = testApplication {
        application {
            module()
        }
        val response = client.get("/misc")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}