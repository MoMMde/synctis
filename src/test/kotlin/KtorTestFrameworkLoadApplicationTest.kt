import io.ktor.server.testing.*
import xyz.mommde.synctis.module
import kotlin.test.Test

class KtorTestFrameworkLoadApplicationTest {
    @Test
    fun canLoadTestFramework() = testApplication {

    }

    @Test
    fun canLoadTestFrameworkWithModule() = testApplication {
        application {
            module()
        }
    }
}