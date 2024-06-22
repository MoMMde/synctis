import io.ktor.server.testing.*
import org.junit.Test
import template.group.name.module

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