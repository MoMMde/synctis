package untis.legacy

import io.ktor.client.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.get
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
        val instance = WebUntisLegacyClient(get(HttpClient::class.java))
        assertNotNull(instance)
    }

    @Test
    fun `login and logout of WebUntis`() {
        runBlocking {
            val legacyClient by inject<WebUntisLegacyClient>()
            legacyClient.login()
            legacyClient.logout()
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}