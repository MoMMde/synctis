package untis.legacy

import io.ktor.server.testing.*
import junit.framework.TestCase.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest
import org.koin.test.inject
import xyz.mommde.synctis.module
import xyz.mommde.synctis.untis.legacy.WebUntisLegacyClient
import xyz.mommde.synctis.webUntisModule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class WebUntisLegacyInstanceTest : KoinTest {

    @BeforeTest
    fun setup(){
        startKoin {
            modules(webUntisModule)
        }
    }

    @Test
    fun `can create legacy instance of WebUntis Legacy`() {
        val koinApp = startKoin {
            modules(webUntisModule)
        }
        val instance = WebUntisLegacyClient(koinApp.koin.get())
        assertNotNull(instance)
    }

    @Test
    fun `login to WebUntis Legacy with fake backend`() = testApplication {
        application {
            module()
        }

        externalServices {

        }
        val legacyClient by inject<WebUntisLegacyClient>()
        legacyClient.login()

    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}